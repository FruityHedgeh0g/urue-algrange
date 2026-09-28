import React from "react";
import { DEFAULT_IMAGE_SRC } from "../../../features/medias/mediaImage";
import styles from "./SplitPanel.module.css";

export interface SplitPanelProps {
  eyebrow?: string;
  title: string;
  /** Texte et éléments du panneau marine. */
  aside?: React.ReactNode;
  /** Titre du panneau de contenu (h1 de la page). */
  heading: string;
  intro?: React.ReactNode;
  children: React.ReactNode;
}

/**
 * Page en deux panneaux : message de marque sur fond marine à gauche,
 * formulaire à droite. Utilisé par la connexion, l'inscription et le contact.
 */
export const SplitPanel: React.FC<SplitPanelProps> = ({ eyebrow, title, aside, heading, intro, children }) => (
  <div className={styles.wrapper}>
    <div className={styles.split}>
      <aside className={styles.aside}>
        <img className={styles.emblem} src={DEFAULT_IMAGE_SRC} alt="" aria-hidden="true" />
        <span className={styles.ribbon} aria-hidden="true" />
        <div className={styles.asideInner}>
          {eyebrow && <p className={styles.eyebrow}>{eyebrow}</p>}
          <p className={styles.asideTitle}>{title}</p>
          {aside && <div className={styles.asideBody}>{aside}</div>}
        </div>
      </aside>
      <section className={styles.content}>
        <h1 className={styles.heading}>{heading}</h1>
        {intro && <div className={styles.intro}>{intro}</div>}
        {children}
      </section>
    </div>
  </div>
);

export default SplitPanel;
