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
      </nav>
      <Outlet />
    </div>
  );
};

export default AccountLayout;
