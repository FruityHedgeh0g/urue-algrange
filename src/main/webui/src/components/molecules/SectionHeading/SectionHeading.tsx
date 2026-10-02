import React from "react";
import styles from "./SectionHeading.module.css";

export interface SectionHeadingProps {
  eyebrow?: string;
  title: string;
  lead?: React.ReactNode;
  align?: "start" | "center";
  /** Version claire pour les bandeaux marine. */
  inverse?: boolean;
  as?: "h1" | "h2" | "h3";
  id?: string;
}

/** Titre de section de la charte : sur-titre marine à filet rouge, titre en capitales, chapô. */
export const SectionHeading: React.FC<SectionHeadingProps> = ({
  eyebrow,
  title,
  lead,
  align = "start",
  inverse = false,
  as: Heading = "h2",
  id,
}) => (
  <div className={`${styles.heading} ${styles[align]}${inverse ? ` ${styles.inverse}` : ""}`}>
    {eyebrow && <p className="eyebrow">{eyebrow}</p>}
    <Heading id={id} className={styles.title}>
      {title}
    </Heading>
    {lead && <p className={styles.lead}>{lead}</p>}
  </div>
);

export default SectionHeading;
