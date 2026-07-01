import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../../../auth/AuthContext";
import styles from "./AccountLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

export const AccountLayout: React.FC = () => {
  const { hasAtLeastRole } = useAuth();

  return (
    <div className="container">
      <h1>Mon espace</h1>
      <nav className={styles.tabs} aria-label="Mon espace">
        <NavLink className={navLinkClass} to="/mon-compte" end>
          Mon profil
        </NavLink>
        <NavLink className={navLinkClass} to="/mon-compte/evenements">
          Mes événements
        </NavLink>
        {hasAtLeastRole("chef_de_groupe") && (
          <NavLink className={navLinkClass} to="/mon-compte/secteur">
            Mon secteur
          </NavLink>
        )}
        {hasAtLeastRole("bureau") && (
          <>
            <NavLink className={navLinkClass} to="/mon-compte/membres">
              Inscrits
            </NavLink>
            <NavLink className={navLinkClass} to="/mon-compte/secteurs">
              Secteurs
            </NavLink>
            <NavLink className={navLinkClass} to="/mon-compte/gestion-evenements">
              Gestion événements
            </NavLink>
            <NavLink className={navLinkClass} to="/mon-compte/roles">
              Rôles
            </NavLink>
            <NavLink className={navLinkClass} to="/mon-compte/demandes-fonctionnalites">
              Demandes
            </NavLink>
          </>
        )}
        {hasAtLeastRole("admin") && (
          <>
            <NavLink className={navLinkClass} to="/mon-compte/configuration">
              Configuration
            </NavLink>
            <NavLink className={navLinkClass} to="/mon-compte/fonctionnalites">
              Fonctionnalités
            </NavLink>
          </>
        )}
      </nav>
      <Outlet />
    </div>
  );
};

export default AccountLayout;
