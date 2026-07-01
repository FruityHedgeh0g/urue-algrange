import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../../../auth/AuthContext";
import styles from "./AdminLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

/** Sous-espace "Administration" (Bureau et supérieur) : regroupe les écrans de gestion sous une seule entrée de "Mon espace". */
export const AdminLayout: React.FC = () => {
  const { hasAtLeastRole } = useAuth();

  return (
    <div>
      <h2 className={styles.title}>Administration</h2>
      <nav className={styles.tabs} aria-label="Administration">
        <NavLink className={navLinkClass} to="/mon-compte/administration/membres">
          Inscrits
        </NavLink>
        <NavLink className={navLinkClass} to="/mon-compte/administration/secteurs">
          Secteurs
        </NavLink>
        <NavLink className={navLinkClass} to="/mon-compte/administration/evenements">
          Gestion événements
        </NavLink>
        <NavLink className={navLinkClass} to="/mon-compte/administration/roles">
          Rôles
        </NavLink>
        <NavLink className={navLinkClass} to="/mon-compte/administration/demandes">
          Demandes
        </NavLink>
        <NavLink className={navLinkClass} to="/mon-compte/administration/carrousel">
          Carrousel
        </NavLink>
        {hasAtLeastRole("admin") && (
          <>
            <NavLink className={navLinkClass} to="/mon-compte/administration/configuration">
              Configuration
            </NavLink>
            <NavLink className={navLinkClass} to="/mon-compte/administration/fonctionnalites">
              Fonctionnalités
            </NavLink>
          </>
        )}
      </nav>
      <Outlet />
    </div>
  );
};

export default AdminLayout;
