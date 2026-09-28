import React from "react";
import { DEFAULT_IMAGE_SRC } from "../../../features/medias/mediaImage";
import styles from "./PageHero.module.css";

export interface PageHeroProps {
  eyebrow?: string;
  title: string;
  lead?: React.ReactNode;
  /** Image de fond (ex : bannière d'une actualité) ; sinon l'emblème de l'association en filigrane. */
  imageSrc?: string;
  /** Contenu sous le titre (boutons, métadonnées). */
  children?: React.ReactNode;
  /** Hauteur réduite, pour les espaces membre et administration. */
  compact?: boolean;
}

/** Bandeau d'en-tête de page : fond marine, bande diagonale rouge, titre en capitales. */
export const PageHero: React.FC<PageHeroProps> = ({ eyebrow, title, lead, imageSrc, children, compact = false }) => (
  <section className={`${styles.hero}${compact ? ` ${styles.compact}` : ""}${imageSrc ? ` ${styles.withImage}` : ""}`}>
    {imageSrc ? (
      <img className={styles.backdrop} src={imageSrc} alt="" aria-hidden="true" />
    ) : (
      <img className={styles.emblem} src={DEFAULT_IMAGE_SRC} alt="" aria-hidden="true" />
    )}
    <span className={styles.ribbon} aria-hidden="true" />
    <div className={styles.inner}>
      {eyebrow && <p className={styles.eyebrow}>{eyebrow}</p>}
      <h1 className={styles.title}>{title}</h1>
      {lead && <p className={styles.lead}>{lead}</p>}
      {children && <div className={styles.extra}>{children}</div>}
    </div>
  </section>
);

export default PageHero;
