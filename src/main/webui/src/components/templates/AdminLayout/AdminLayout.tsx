import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAccess } from "../../../auth/useAccess";
import styles from "./AdminLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

/** Espace "Administration" (Bureau et supérieur), accessible depuis la barre de navigation principale. */
export const AdminLayout: React.FC = () => {
  const { navFor } = useAccess();

  return (
    <div className="container">
      <h1 className={styles.title}>Administration</h1>
      <nav className={styles.tabs} aria-label="Administration">
        {navFor("admin").map((e) => (
          <NavLink key={e.id} className={navLinkClass} to={e.path} end={e.end}>
            {e.label}
          </NavLink>
        ))}
      </nav>
      <Outlet />
    </div>
  );
};

export default AdminLayout;
