import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAccess } from "../../../auth/useAccess";
import { AccessSection } from "../../../auth/access";
import PageHero from "../../organisms/PageHero/PageHero";
import styles from "./SpaceLayout.module.css";

const navLinkClass = ({ isActive }: { isActive: boolean }) => `${styles.tab}${isActive ? ` ${styles.active}` : ""}`;

export interface SpaceLayoutProps {
  section: AccessSection;
  eyebrow: string;
  title: string;
  lead?: React.ReactNode;
}

/** Gabarit des espaces "Mon espace" et "Administration" : bandeau, onglets issus de la carte d'accès, contenu. */
export const SpaceLayout: React.FC<SpaceLayoutProps> = ({ section, eyebrow, title, lead }) => {
  const { navFor } = useAccess();

  return (
    <>
      <PageHero compact eyebrow={eyebrow} title={title} lead={lead} />
      <div className={styles.tabsBar}>
        <nav className={styles.tabs} aria-label={title}>
          {navFor(section).map((e) => (
            <NavLink key={e.id} className={navLinkClass} to={e.path} end={e.end}>
              {e.label}
            </NavLink>
          ))}
        </nav>
      </div>
      <div className={styles.content}>
        <Outlet />
      </div>
    </>
  );
};

export default SpaceLayout;
