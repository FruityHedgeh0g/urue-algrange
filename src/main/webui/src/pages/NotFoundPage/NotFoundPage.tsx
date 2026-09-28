import React from "react";
import ButtonLink from "../../components/atoms/ButtonLink/ButtonLink";
import styles from "./NotFoundPage.module.css";

export const NotFoundPage: React.FC = () => (
  <section className={styles.section}>
    <p className={styles.code} aria-hidden="true">
      4<span>0</span>4
    </p>
    <h1 className={styles.title}>Page introuvable</h1>
    <p className={styles.text}>Cette page n'existe pas ou n'est pas encore disponible.</p>
    <ButtonLink to="/" label="Retour à l'accueil" variant="accent" arrow />
  </section>
);

export default NotFoundPage;
