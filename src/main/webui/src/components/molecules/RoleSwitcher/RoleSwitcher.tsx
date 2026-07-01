import React from "react";
import { useAuth } from "../../../auth/AuthContext";
import { ROLE_HIERARCHY, ROLE_LABELS, RoleId } from "../../../auth/roles";
import styles from "./RoleSwitcher.module.css";

/**
 * Sélecteur de rôle de prévisualisation, tant que l'authentification réelle
 * n'est pas branchée sur le backend. N'apparaît jamais en build de production.
 */
export const RoleSwitcher: React.FC = () => {
  const { role, setRole } = useAuth();

  return (
    <label className={styles.switcher}>
      <span className={styles.label}>Aperçu</span>
      <select
        className={styles.select}
        value={role}
        onChange={(e) => setRole(e.target.value as RoleId)}
      >
        {ROLE_HIERARCHY.map((r) => (
          <option key={r} value={r}>
            {ROLE_LABELS[r]}
          </option>
        ))}
      </select>
    </label>
  );
};

export default RoleSwitcher;
