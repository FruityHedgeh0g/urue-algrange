import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Logo from "../../atoms/Logo/Logo";
import DropdownMenu from "../../molecules/DropdownMenu/DropdownMenu";
import ThemeToggle from "../../molecules/ThemeToggle/ThemeToggle";
import RoleSwitcher from "../../molecules/RoleSwitcher/RoleSwitcher";
import { useAuth } from "../../../auth/AuthContext";
import styles from "./Header.module.css";

export const Header: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const { isAuthenticated, setRole } = useAuth();
  const navigate = useNavigate();

  const handleAuthClick = () => {
    if (isAuthenticated) {
      setRole("visiteur");
      navigate("/");
    } else {
      navigate("/connexion");
    }
  };

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
            label="Association"
            items={[
              { label: "Qui sommes-nous ?", to: "/#about" },
              { label: "Actualités", to: "/actualites" },
              { label: "Galerie photos", to: "/galerie" },
              { label: "Contact", to: "/contact" },
            ]}
          />
          <Link className={styles.navLink} to="/evenements">
            Événements
          </Link>
          <DropdownMenu
            label="Soutenir"
            items={[
              { label: "Faire un don", to: "/don" },
              { label: "Devenir bénévole", to: "/#benevolat" },
            ]}
          />
          {isAuthenticated && (
            <Link className={styles.navLink} to="/mon-compte">
              Mon espace
            </Link>
          )}
        </nav>
      </div>

      <div className={styles.center}>
        <Logo />
      </div>

      <div className={styles.right}>
        {import.meta.env.DEV && (
          <div className={styles.devOnly}>
            <RoleSwitcher />
          </div>
        )}
        <ThemeToggle />
        <button type="button" className={styles.loginBtn} onClick={handleAuthClick}>
          {isAuthenticated ? "Se déconnecter" : "Se connecter"}
        </button>
      </div>
    </header>
  );
};

export default Header;
