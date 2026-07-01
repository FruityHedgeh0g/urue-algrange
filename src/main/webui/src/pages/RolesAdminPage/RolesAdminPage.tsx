import React, { useState } from "react";
import { useRoleMutations, useRoles } from "../../features/roles/useRoles";
import { PROTECTED_ROLE_NAMES } from "../../features/roles/types";
import { useAuth } from "../../auth/AuthContext";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./RolesAdminPage.module.css";

interface Draft {
  name: string;
  description: string;
}

const emptyDraft: Draft = { name: "", description: "" };

export const RolesAdminPage: React.FC = () => {
  const { data: roles, isLoading } = useRoles();
  const { hasAtLeastRole } = useAuth();
  const { update, create } = useRoleMutations();
  const [editingId, setEditingId] = useState<string | null>(null);
  const [draft, setDraft] = useState<Draft>(emptyDraft);
  const [creating, setCreating] = useState(false);
  const [newRole, setNewRole] = useState<Draft>(emptyDraft);

  if (isLoading) return <Spinner label="Chargement des rôles..." />;

  const isAdmin = hasAtLeastRole("admin");
  const visibleRoles = (roles ?? []).filter((role) => isAdmin || !PROTECTED_ROLE_NAMES.includes(role.name));

  const startEdit = (roleId: string) => {
    const role = roles?.find((r) => r.roleId === roleId);
    if (!role) return;
    setEditingId(roleId);
    setDraft({ name: role.name, description: role.description });
  };

  const handleSave = (roleId: string) => (e: React.FormEvent) => {
    e.preventDefault();
    update.mutate({ roleId, ...draft }, { onSuccess: () => setEditingId(null) });
  };

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    create.mutate(newRole, {
      onSuccess: () => {
        setNewRole(emptyDraft);
        setCreating(false);
      },
    });
  };

  return (
    <div className={styles.wrapper}>
      <Button label={creating ? "Annuler" : "Nouveau rôle"} variant={creating ? "outline" : "accent"} onClick={() => setCreating((v) => !v)} />

      {creating && (
        <form className={styles.form} onSubmit={handleCreate} noValidate>
          <FormField label="Nom du rôle" value={newRole.name} onChange={(e) => setNewRole({ ...newRole, name: e.target.value })} required />
          <FormField
            label="Description"
            multiline
            rows={3}
            value={newRole.description}
            onChange={(e) => setNewRole({ ...newRole, description: e.target.value })}
          />
          <Button type="submit" label="Créer le rôle" disabled={create.isPending} />
        </form>
      )}

      <ul className={styles.list}>
        {visibleRoles.map((role) => {
          const isPresident = role.name === "Président";
          return (
            <AdminListItem
              key={role.roleId}
              title={role.name}
              subtitle={role.description}
              editing={editingId === role.roleId}
              onToggleEdit={() => (editingId === role.roleId ? setEditingId(null) : startEdit(role.roleId))}
              editDisabled={isPresident}
              editDisabledReason={isPresident ? "Nécessite une validation à deux administrateurs (à venir)" : undefined}
            >
              <form className={styles.form} onSubmit={handleSave(role.roleId)} noValidate>
                <FormField label="Nom du rôle" value={draft.name} onChange={(e) => setDraft({ ...draft, name: e.target.value })} required />
                <FormField
                  label="Description"
                  multiline
                  rows={3}
                  value={draft.description}
                  onChange={(e) => setDraft({ ...draft, description: e.target.value })}
                />
                <Button type="submit" label="Enregistrer" disabled={update.isPending} />
              </form>
            </AdminListItem>
          );
        })}
      </ul>
    </div>
  );
};

export default RolesAdminPage;
