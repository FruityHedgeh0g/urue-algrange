import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import styles from "./AccountLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

export const AccountLayout: React.FC = () => (
  <div className="container">
    <h1>Mon espace</h1>
    <nav className={styles.tabs} aria-label="Mon espace">
      <NavLink className={navLinkClass} to="/mon-compte" end>
        Mon profil
      </NavLink>
      <NavLink className={navLinkClass} to="/mon-compte/evenements">
        Mes événements
      </NavLink>
    </nav>
    <Outlet />
  </div>
);

export default AccountLayout;
