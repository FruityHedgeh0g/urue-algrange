import React, { useState } from "react";
import { useAllMembers, useUpdateMember } from "../../features/users/useMembers";
import { useSectors } from "../../features/sectors/useSector";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./MembersAdminPage.module.css";

interface EditState {
  firstName: string;
  lastName: string;
  groupId: string;
}

export const MembersAdminPage: React.FC = () => {
  const { data: members, isLoading } = useAllMembers();
  const { data: sectors } = useSectors();
  const updateMember = useUpdateMember();
  const [editingId, setEditingId] = useState<string | null>(null);
  const [draft, setDraft] = useState<EditState | null>(null);

  const groupOptions = (sectors ?? []).flatMap((sector) => sector.groups.map((g) => ({ ...g, sectorName: sector.name })));

  if (isLoading) return <Spinner label="Chargement des inscrits..." />;

  const startEdit = (memberId: string) => {
    const member = members?.find((m) => m.userId === memberId);
    if (!member) return;
    setEditingId(memberId);
    setDraft({ firstName: member.firstName, lastName: member.lastName, groupId: member.groupId });
  };

  const handleSave = (userId: string) => (e: React.FormEvent) => {
    e.preventDefault();
    if (!draft) return;
    updateMember.mutate({ userId, ...draft }, { onSuccess: () => setEditingId(null) });
  };

  return (
    <ul className={styles.list}>
      {members?.map((member) => {
        const group = groupOptions.find((g) => g.groupId === member.groupId);
        return (
          <AdminListItem
            key={member.userId}
            title={`${member.firstName} ${member.lastName}`}
            subtitle={group ? `${group.name} · ${group.sectorName}` : undefined}
            editing={editingId === member.userId}
            onToggleEdit={() => (editingId === member.userId ? setEditingId(null) : startEdit(member.userId))}
          >
            {draft && (
              <form className={styles.form} onSubmit={handleSave(member.userId)} noValidate>
                <FormField label="Prénom" value={draft.firstName} onChange={(e) => setDraft({ ...draft, firstName: e.target.value })} required />
                <FormField label="Nom" value={draft.lastName} onChange={(e) => setDraft({ ...draft, lastName: e.target.value })} required />
                <Select
                  label="Groupe"
                  value={draft.groupId}
                  onChange={(groupId) => setDraft({ ...draft, groupId })}
                  options={groupOptions.map((g) => ({ value: g.groupId, label: `${g.name} (${g.sectorName})` }))}
                />
                <Button type="submit" label="Enregistrer" disabled={updateMember.isPending} />
              </form>
            )}
          </AdminListItem>
        );
      })}
    </ul>
  );
};

export default MembersAdminPage;
