import React from "react";
import { Link } from "react-router-dom";
import { useNavbarLogo } from "../../../features/configurations/useNavbarLogo";
import styles from "./Logo.module.css";

export const Logo: React.FC = () => {
  const { src, alt } = useNavbarLogo();
  return (
    <Link className={styles.logo} to="/" aria-label="Accueil - Une Rose Un Espoir Algrange">
      <img src={src} alt={alt} />
    </Link>
  );
};

export default Logo;
