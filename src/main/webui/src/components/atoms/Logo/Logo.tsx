import React from "react";
import { Link } from "react-router-dom";
import { useNavbarLogo } from "../../../features/configurations/useNavbarLogo";
import styles from "./Logo.module.css";

export interface LogoProps {
  /** Affiche le nom de l'association à côté de l'emblème. */
  withWordmark?: boolean;
  /** Version claire, pour les bandeaux marine. */
  inverse?: boolean;
}

export const Logo: React.FC<LogoProps> = ({ withWordmark = true, inverse = false }) => {
  const { src, alt } = useNavbarLogo();
  return (
    <Link
      className={`${styles.logo}${inverse ? ` ${styles.inverse}` : ""}`}
      to="/"
      aria-label="Accueil - Une Rose Un Espoir Algrange"
    >
      <span className={styles.mark}>
        <img src={src} alt={alt} />
      </span>
      {withWordmark && (
        <span className={styles.wordmark} aria-hidden="true">
          <span className={styles.name}>Une Rose Un Espoir</span>
          <span className={styles.place}>Algrange</span>
        </span>
      )}
    </Link>
  );
};

export default Logo;
