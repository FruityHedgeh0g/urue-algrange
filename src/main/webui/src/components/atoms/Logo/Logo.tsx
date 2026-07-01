import React from "react";
import { Link } from "react-router-dom";
import styles from "./Logo.module.css";

export const Logo: React.FC = () => (
  <Link className={styles.logo} to="/" aria-label="Accueil - Une Rose Un Espoir Algrange">
    <img src={`${import.meta.env.BASE_URL}logo_asso_transparent.png`} alt="Une Rose Un Espoir - Algrange" />
  </Link>
);

export default Logo;
