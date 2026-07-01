import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../../../auth/AuthContext";
import styles from "./AdminLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

/** Espace "Administration" (Bureau et supérieur), accessible depuis la barre de navigation principale. */
export const AdminLayout: React.FC = () => {
  const { hasAtLeastRole } = useAuth();

  return (
    <div className="container">
      <h1 className={styles.title}>Administration</h1>
      <nav className={styles.tabs} aria-label="Administration">
        <NavLink className={navLinkClass} to="/administration/membres">
          Inscrits
        </NavLink>
        <NavLink className={navLinkClass} to="/administration/secteurs">
          Secteurs
        </NavLink>
        <NavLink className={navLinkClass} to="/administration/evenements">
          Gestion événements
        </NavLink>
        <NavLink className={navLinkClass} to="/administration/roles">
          Rôles
        </NavLink>
        <NavLink className={navLinkClass} to="/administration/demandes">
          Demandes
        </NavLink>
        <NavLink className={navLinkClass} to="/administration/carrousel">
          Carrousel
        </NavLink>
        {hasAtLeastRole("admin") && (
          <>
            <NavLink className={navLinkClass} to="/administration/configuration">
              Configuration
            </NavLink>
            <NavLink className={navLinkClass} to="/administration/fonctionnalites">
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
