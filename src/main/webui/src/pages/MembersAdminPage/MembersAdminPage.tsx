import React from "react";
import { useAllMembers, useAppointPresident, useChangeRole, useUpdateMember } from "../../features/users/useMembers";
import { MemberInput } from "../../features/users/usersApi";
import { useAuth } from "../../auth/AuthContext";
import { useSectors } from "../../features/sectors/useSector";
import { assignableRoles, ROLE_LABELS, RoleId, roleAtLeast } from "../../auth/roles";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Checkbox from "../../components/atoms/Checkbox/Checkbox";
import Spinner from "../../components/atoms/Spinner/Spinner";

/** `sectorId` : le Secteur que nomme le Super admin (nouvel Admin, ou premier Secteur). */
type MemberDraft = MemberInput & { role: RoleId; president: boolean; sectorId: string };

export const MembersAdminPage: React.FC = () => {
  const { data: members, isLoading } = useAllMembers();
  const { role: viewerRole, user } = useAuth();
  const updateMember = useUpdateMember();
  const changeRole = useChangeRole();
  const appointPresident = useAppointPresident();
  const { data: sectors } = useSectors();
  const sectorName = (sectorId?: string | null) => sectors?.find((s) => s.sectorId === sectorId)?.name;

  /** Le Super admin nomme le Secteur d'un nouvel Admin, ou d'une personne qui n'en a pas encore (ADR 0004). */
  const namesSector = (member: { role: RoleId; sectorId?: string | null }, draft: MemberDraft) =>
    viewerRole === "super_admin" &&
    draft.role !== member.role &&
    roleAtLeast(draft.role, "membre") &&
    (draft.role === "admin" || !member.sectorId);

  /** Rôles proposés pour une personne : aucun sur soi-même (chaîne de promotion). */
  const rolesFor = (userId: string, current: RoleId) => (userId === user?.userId ? [] : assignableRoles(viewerRole, current));

  /** Seul un Admin désigne le Président, parmi les membres du Bureau qui ne le sont pas déjà. */
  const canAppointPresident = (member: { role: RoleId; president?: boolean }) =>
    roleAtLeast(viewerRole, "admin") && member.role === "bureau" && !member.president;

  const save = async (userId: string, { role, president, sectorId, ...profile }: MemberDraft) => {
    await updateMember.mutateAsync({ userId, ...profile });
    const currentMember = members?.find((m) => m.userId === userId);
    // Un changement de rôle fait quitter le Bureau : le titre de Président ne s'applique plus
    if (role !== currentMember?.role) await changeRole.mutateAsync({ userId, role, sectorId: sectorId || undefined });
    else if (president && !currentMember?.president) await appointPresident.mutateAsync(userId);
  };

  if (isLoading) return <Spinner label="Chargement des inscrits..." />;

  return (
    <AdminCrudList
      items={members ?? []}
      idOf={(m) => m.userId}
      display={(member) => ({
        title: `${member.firstName} ${member.lastName}`,
        subtitle: [ROLE_LABELS[member.role], member.president && "Président", sectorName(member.sectorId)].filter(Boolean).join(" · "),
      })}
      toDraft={(m): MemberDraft => ({
        firstName: m.firstName,
        lastName: m.lastName,
        role: m.role,
        president: Boolean(m.president),
        sectorId: m.sectorId ?? "",
      })}
      renderFields={(draft, setDraft, member) => {
        const roles = member ? rolesFor(member.userId, member.role) : [];
        return (
          <>
            <FormField label="Prénom" value={draft.firstName} onChange={(e) => setDraft({ ...draft, firstName: e.target.value })} required />
            <FormField label="Nom" value={draft.lastName} onChange={(e) => setDraft({ ...draft, lastName: e.target.value })} required />
            {roles.length > 0 && (
              <Select
                label="Rôle"
                value={draft.role}
                onChange={(role) => setDraft({ ...draft, role: role as RoleId })}
                options={roles.map((r) => ({ value: r, label: ROLE_LABELS[r] }))}
              />
            )}
            {member && namesSector(member, draft) && (
              <Select
                label="Secteur"
                value={draft.sectorId}
                onChange={(sectorId) => setDraft({ ...draft, sectorId })}
                options={[{ value: "", label: "Choisissez le secteur" }, ...(sectors ?? []).map((s) => ({ value: s.sectorId, label: s.name }))]}
              />
            )}
            {member && canAppointPresident(member) && (
              <Checkbox label="Président" checked={draft.president} onChange={(president) => setDraft({ ...draft, president })} />
            )}
          </>
        );
      }}
      onUpdate={save}
    />
  );
};

export default MembersAdminPage;
