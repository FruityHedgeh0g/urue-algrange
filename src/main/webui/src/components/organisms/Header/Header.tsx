import React, { useState } from "react";
import Logo from "../../atoms/Logo/Logo";
import DropdownMenu from "../../molecules/DropdownMenu/DropdownMenu";
import ThemeToggle from "../../molecules/ThemeToggle/ThemeToggle";
import RoleSwitcher from "../../molecules/RoleSwitcher/RoleSwitcher";
import styles from "./Header.module.css";

export const Header: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className={styles.header}>
      <div className={styles.left}>
        <button
          type="button"
          className={styles.burger}
          aria-label={menuOpen ? "Fermer le menu" : "Ouvrir le menu"}
          aria-expanded={menuOpen}
          onClick={() => setMenuOpen((v) => !v)}
        >
          <span />
          <span />
          <span />
        </button>

        <nav className={`${styles.navLinks}${menuOpen ? ` ${styles.open}` : ""}`} aria-label="Principal">
          <DropdownMenu
            label="Services"
            items={[
              { label: "Collecte 2025", href: "#collecte" },
              { label: "Bénévolat", href: "#benevolat" },
              { label: "Dons", href: "#don" },
            ]}
          />
          <DropdownMenu
            label="Association"
            items={[
              { label: "Qui sommes-nous ?", href: "#about" },
              { label: "Équipe", href: "#team" },
              { label: "Contact", href: "#contact" },
            ]}
          />
          <DropdownMenu
            label="Actualités"
            items={[
              { label: "Événements", href: "#events" },
              { label: "Blog", href: "#blog" },
            ]}
          />
        </nav>
      </div>

      <div className={styles.center}>
        <Logo />
      </div>

      <div className={styles.right}>
        {import.meta.env.DEV && <RoleSwitcher />}
        <ThemeToggle />
        <button type="button" className={styles.loginBtn} onClick={() => alert("Connexion à venir")}>
          Se connecter
        </button>
      </div>
    </header>
  );
};

export default Header;
