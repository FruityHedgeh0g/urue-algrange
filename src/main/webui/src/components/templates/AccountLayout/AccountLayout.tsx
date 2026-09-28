import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAccess } from "../../../auth/useAccess";
import styles from "./AccountLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

export const AccountLayout: React.FC = () => {
  const { navFor } = useAccess();

  return (
    <div className="container">
      <h1>Mon espace</h1>
      <nav className={styles.tabs} aria-label="Mon espace">
        {navFor("account").map((e) => (
          <NavLink key={e.id} className={navLinkClass} to={e.path} end={e.end}>
            {e.label}
          </NavLink>
        ))}
      </nav>
      <Outlet />
    </div>
  );
};

export default AccountLayout;
