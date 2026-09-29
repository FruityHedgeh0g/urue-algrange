import React from "react";
import { useGroupMutations, useGroups } from "../../features/groups/useGroups";
import { GroupInput } from "../../features/groups/groupsApi";
import { useSectors } from "../../features/sectors/useSector";
import { useAllMembers } from "../../features/users/useMembers";
import { canLeadGroupe } from "../../auth/roles";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";

/** `chefId` vide : le Groupe n'a pas de chef. */
type GroupDraft = GroupInput & { chefId: string };

export const GroupsAdminPage: React.FC = () => {
  const { data: groups, isLoading } = useGroups();
  const { data: sectors } = useSectors();
  const { data: members } = useAllMembers();
  const { create, update, affectation } = useGroupMutations();

  /** Seules les personnes au moins Chef de groupe (Bureau compris) peuvent mener un Groupe. */
  const chefOptions = (groupId?: string) => [
    { value: "", label: "Aucun chef" },
    ...(members ?? [])
      .filter((m) => canLeadGroupe(m.role))
      .map((m) => {
        const led = groups?.find((g) => g.chef?.userId === m.userId && g.groupId !== groupId);
        return { value: m.userId, label: `${m.firstName} ${m.lastName}${led ? ` (mène ${led.name})` : ""}` };
      }),
  ];

  const sectorOptions = (sectors ?? []).map((s) => ({ value: s.sectorId, label: s.name }));
  const emptyDraft: GroupDraft = { name: "", description: "", area: "", sectorId: sectorOptions[0]?.value ?? "", chefId: "" };

  const save = async (groupId: string, { chefId, ...input }: GroupDraft) => {
    await update.mutateAsync({ groupId, ...input });
    const current = groups?.find((g) => g.groupId === groupId)?.chef?.userId ?? "";
    if (chefId === current) return;
    const chef = members?.find((m) => m.userId === chefId);
    await affectation.mutateAsync({
      groupId,
      chef: chef ? { userId: chef.userId, firstName: chef.firstName, lastName: chef.lastName } : null,
    });
  };

  if (isLoading) return <Spinner label="Chargement des groupes..." />;

  return (
    <AdminCrudList
      title="Groupes"
      items={groups ?? []}
      idOf={(g) => g.groupId}
      display={(g) => {
        const chef = g.chef ? `Chef : ${g.chef.firstName} ${g.chef.lastName}` : "Sans chef";
        return { title: g.name, subtitle: [g.area, chef].filter(Boolean).join(" · ") };
      }}
      toDraft={(g): GroupDraft => ({
        name: g.name,
        description: g.description,
        area: g.area,
        sectorId: g.sectorId,
        chefId: g.chef?.userId ?? "",
      })}
      renderFields={(draft, setDraft, group) => (
        <>
          <FormField label="Nom du groupe" value={draft.name} onChange={(e) => setDraft({ ...draft, name: e.target.value })} required />
          <FormField
            label="Description"
            multiline
            rows={3}
            value={draft.description}
            onChange={(e) => setDraft({ ...draft, description: e.target.value })}
          />
          <FormField label="Zone couverte" value={draft.area} onChange={(e) => setDraft({ ...draft, area: e.target.value })} />
          <Select label="Secteur" value={draft.sectorId} onChange={(sectorId) => setDraft({ ...draft, sectorId })} options={sectorOptions} />
          {group && (
            <Select
              label="Chef de groupe"
              value={draft.chefId}
              onChange={(chefId) => setDraft({ ...draft, chefId })}
              options={chefOptions(group.groupId)}
            />
          )}
        </>
      )}
      onUpdate={save}
      create={{
        buttonLabel: "+ Nouveau groupe",
        submitLabel: "Créer le groupe",
        emptyDraft,
        onCreate: ({ chefId: _chefId, ...input }) => create.mutateAsync(input),
      }}
    />
  );
};

export default GroupsAdminPage;
