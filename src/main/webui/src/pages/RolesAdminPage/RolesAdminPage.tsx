import React from "react";
import { useRoleMutations, useRoles } from "../../features/roles/useRoles";
import { RoleInput } from "../../features/roles/rolesApi";
import { PERMISSION_FEATURES, PROTECTED_ROLE_NAMES } from "../../features/roles/types";
import { useAuth } from "../../auth/AuthContext";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Badge from "../../components/atoms/Badge/Badge";
import Checkbox from "../../components/atoms/Checkbox/Checkbox";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./RolesAdminPage.module.css";

const emptyDraft: RoleInput = { name: "", description: "", permissions: [] };

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

  if (isLoading) return <Spinner label="Chargement des rôles..." />;

  const isAdmin = hasAtLeastRole("admin");
  const visibleRoles = (roles ?? []).filter((role) => isAdmin || !PROTECTED_ROLE_NAMES.includes(role.name));

  return (
    <AdminCrudList
      title="Rôles"
      items={visibleRoles}
      idOf={(role) => role.roleId}
      display={(role) => {
        const isPresident = role.name === "Président";
        return {
          title: role.name,
          subtitle: role.description,
          editDisabled: isPresident,
          editDisabledReason: isPresident ? "Nécessite une validation à deux administrateurs (à venir)" : undefined,
          footer:
            role.permissions.length > 0 ? (
              role.permissions.map((id) => {
                const feature = PERMISSION_FEATURES.find((f) => f.id === id);
                return feature ? <Badge key={id} label={feature.label} tone="muted" /> : null;
              })
            ) : (
              <span className={styles.noPermissions}>Aucune fonctionnalité d'administration</span>
            ),
        };
      }}
      toDraft={(role): RoleInput => ({ name: role.name, description: role.description, permissions: role.permissions })}
      renderFields={(draft, setDraft) => (
        <>
          <FormField label="Nom du rôle" value={draft.name} onChange={(e) => setDraft({ ...draft, name: e.target.value })} required />
          <FormField
            label="Description"
            multiline
            rows={3}
            value={draft.description}
            onChange={(e) => setDraft({ ...draft, description: e.target.value })}
          />
          <PermissionsField permissions={draft.permissions} onChange={(permissions) => setDraft({ ...draft, permissions })} />
        </>
      )}
      onUpdate={(roleId, draft) => update.mutateAsync({ roleId, ...draft })}
      create={{
        buttonLabel: "+ Nouveau rôle",
        submitLabel: "Créer le rôle",
        emptyDraft,
        onCreate: (draft) => create.mutateAsync(draft),
      }}
    />
  );
};

export default RolesAdminPage;
