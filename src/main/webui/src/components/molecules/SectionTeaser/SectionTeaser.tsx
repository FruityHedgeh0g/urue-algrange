import React from "react";
import { Link } from "react-router-dom";
import styles from "./SectionTeaser.module.css";

export interface SectionTeaserProps {
  id?: string;
  title: string;
  description: string;
  to: string;
  linkLabel: string;
}

/**
 * Bloc de section entièrement cliquable (titre + texte + lien flèche), plutôt
 * qu'un gros bouton. La zone cliquable/survolable couvre toute la largeur de
 * la bande (entre les séparateurs), le contenu texte reste centré à
 * l'intérieur.
 */
export const SectionTeaser: React.FC<SectionTeaserProps> = ({ id, title, description, to, linkLabel }) => (
  <section id={id} className={styles.teaser}>
    <div className={styles.inner}>
      <h2>{title}</h2>
      <p>{description}</p>
      <Link className={styles.link} to={to} aria-label={`${title} — ${linkLabel}`}>
        {linkLabel} →
      </Link>
    </div>
  </section>
);

export default SectionTeaser;
