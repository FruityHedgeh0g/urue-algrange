import React from "react";
import { Link } from "react-router-dom";
import styles from "./NotFoundPage.module.css";

export const NotFoundPage: React.FC = () => (
  <section className={styles.section}>
    <h1>Page introuvable</h1>
    <p>Cette page n'existe pas ou n'est pas encore disponible.</p>
    <Link className={styles.link} to="/">
      Retour à l'accueil
    </Link>
  </section>
);

export default NotFoundPage;
