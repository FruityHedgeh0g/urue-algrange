import React, { useState } from "react";
import { useRoleMutations, useRoles } from "../../features/roles/useRoles";
import { PERMISSION_FEATURES, PROTECTED_ROLE_NAMES } from "../../features/roles/types";
import { useAuth } from "../../auth/AuthContext";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Badge from "../../components/atoms/Badge/Badge";
import Checkbox from "../../components/atoms/Checkbox/Checkbox";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./RolesAdminPage.module.css";

interface Draft {
  name: string;
  description: string;
  permissions: string[];
}

const emptyDraft: Draft = { name: "", description: "", permissions: [] };

function togglePermission(permissions: string[], id: string): string[] {
  return permissions.includes(id) ? permissions.filter((p) => p !== id) : [...permissions, id];
}

interface PermissionsFieldProps {
  permissions: string[];
  onChange: (permissions: string[]) => void;
}

const PermissionsField: React.FC<PermissionsFieldProps> = ({ permissions, onChange }) => (
  <fieldset className={styles.fieldset}>
    <legend className={styles.legend}>Fonctionnalités accessibles</legend>
    <div className={styles.permissionsGrid}>
      {PERMISSION_FEATURES.map((feature) => (
        <Checkbox
          key={feature.id}
          label={feature.label}
          checked={permissions.includes(feature.id)}
          onChange={() => onChange(togglePermission(permissions, feature.id))}
        />
      ))}
    </div>
  </fieldset>
);

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
    setDraft({ name: role.name, description: role.description, permissions: role.permissions });
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
      <div className={styles.toolbar}>
        <h2 className={styles.title}>Rôles</h2>
        <Button label={creating ? "Annuler" : "+ Nouveau rôle"} variant={creating ? "outline" : "primary"} onClick={() => setCreating((v) => !v)} />
      </div>

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
          <PermissionsField permissions={newRole.permissions} onChange={(permissions) => setNewRole({ ...newRole, permissions })} />
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
              footer={
                role.permissions.length > 0 ? (
                  role.permissions.map((id) => {
                    const feature = PERMISSION_FEATURES.find((f) => f.id === id);
                    return feature ? <Badge key={id} label={feature.label} tone="muted" /> : null;
                  })
                ) : (
                  <span className={styles.noPermissions}>Aucune fonctionnalité d'administration</span>
                )
              }
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
                <PermissionsField permissions={draft.permissions} onChange={(permissions) => setDraft({ ...draft, permissions })} />
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
