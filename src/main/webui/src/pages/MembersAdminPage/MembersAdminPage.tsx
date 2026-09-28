import React from "react";
import { useAllMembers, useUpdateMember } from "../../features/users/useMembers";
import { MemberInput } from "../../features/users/usersApi";
import { useSectors } from "../../features/sectors/useSector";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";

export const MembersAdminPage: React.FC = () => {
  const { data: members, isLoading } = useAllMembers();
  const { data: sectors } = useSectors();
  const updateMember = useUpdateMember();

  const groupOptions = (sectors ?? []).flatMap((sector) => sector.groups.map((g) => ({ ...g, sectorName: sector.name })));

  if (isLoading) return <Spinner label="Chargement des inscrits..." />;

  return (
    <AdminCrudList
      items={members ?? []}
      idOf={(m) => m.userId}
      display={(member) => {
        const group = groupOptions.find((g) => g.groupId === member.groupId);
        return {
          title: `${member.firstName} ${member.lastName}`,
          subtitle: group ? `${group.name} · ${group.sectorName}` : undefined,
        };
      }}
      toDraft={(m): MemberInput => ({ firstName: m.firstName, lastName: m.lastName, groupId: m.groupId })}
      renderFields={(draft, setDraft) => (
        <>
          <FormField label="Prénom" value={draft.firstName} onChange={(e) => setDraft({ ...draft, firstName: e.target.value })} required />
          <FormField label="Nom" value={draft.lastName} onChange={(e) => setDraft({ ...draft, lastName: e.target.value })} required />
          <Select
            label="Groupe"
            value={draft.groupId}
            onChange={(groupId) => setDraft({ ...draft, groupId })}
            options={groupOptions.map((g) => ({ value: g.groupId, label: `${g.name} (${g.sectorName})` }))}
          />
        </>
      )}
      onUpdate={(userId, draft) => updateMember.mutateAsync({ userId, ...draft })}
    />
  );
};

export default MembersAdminPage;
