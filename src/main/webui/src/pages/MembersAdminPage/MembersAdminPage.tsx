import React from "react";
import { useAllMembers, useChangeRole, useUpdateMember } from "../../features/users/useMembers";
import { MemberInput } from "../../features/users/usersApi";
import { useSectors } from "../../features/sectors/useSector";
import { useAuth } from "../../auth/AuthContext";
import { assignableRoles, ROLE_LABELS, RoleId } from "../../auth/roles";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";

type MemberDraft = MemberInput & { role: RoleId };

export const MembersAdminPage: React.FC = () => {
  const { data: members, isLoading } = useAllMembers();
  const { data: sectors } = useSectors();
  const { role: viewerRole, user } = useAuth();
  const updateMember = useUpdateMember();
  const changeRole = useChangeRole();

  const groupOptions = (sectors ?? []).flatMap((sector) => sector.groups.map((g) => ({ ...g, sectorName: sector.name })));

  /** Rôles proposés pour une personne : aucun sur soi-même (chaîne de promotion). */
  const rolesFor = (userId: string, current: RoleId) => (userId === user?.userId ? [] : assignableRoles(viewerRole, current));

  const save = async (userId: string, { role, ...profile }: MemberDraft) => {
    await updateMember.mutateAsync({ userId, ...profile });
    const current = members?.find((m) => m.userId === userId)?.role;
    if (role !== current) await changeRole.mutateAsync({ userId, role });
  };

  if (isLoading) return <Spinner label="Chargement des inscrits..." />;

  return (
    <AdminCrudList
      items={members ?? []}
      idOf={(m) => m.userId}
      display={(member) => {
        const group = groupOptions.find((g) => g.groupId === member.groupId);
        return {
          title: `${member.firstName} ${member.lastName}`,
          subtitle: [ROLE_LABELS[member.role], group && `${group.name} · ${group.sectorName}`].filter(Boolean).join(" · "),
        };
      }}
      toDraft={(m): MemberDraft => ({ firstName: m.firstName, lastName: m.lastName, groupId: m.groupId, role: m.role })}
      renderFields={(draft, setDraft, member) => {
        const roles = member ? rolesFor(member.userId, member.role) : [];
        return (
          <>
            <FormField label="Prénom" value={draft.firstName} onChange={(e) => setDraft({ ...draft, firstName: e.target.value })} required />
            <FormField label="Nom" value={draft.lastName} onChange={(e) => setDraft({ ...draft, lastName: e.target.value })} required />
            <Select
              label="Groupe"
              value={draft.groupId}
              onChange={(groupId) => setDraft({ ...draft, groupId })}
              options={groupOptions.map((g) => ({ value: g.groupId, label: `${g.name} (${g.sectorName})` }))}
            />
            {roles.length > 0 && (
              <Select
                label="Rôle"
                value={draft.role}
                onChange={(role) => setDraft({ ...draft, role: role as RoleId })}
                options={roles.map((r) => ({ value: r, label: ROLE_LABELS[r] }))}
              />
            )}
          </>
        );
      }}
      onUpdate={save}
    />
  );
};

export default MembersAdminPage;
