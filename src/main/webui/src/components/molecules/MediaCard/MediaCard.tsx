import React from "react";
import { Link } from "react-router-dom";
import styles from "./MediaCard.module.css";

export interface MediaCardProps {
  to: string;
  imageSrc: string;
  imageAlt: string;
  title: string;
  subtitle?: string;
  excerpt?: string;
  badge?: React.ReactNode;
  /** Niveau de titre sémantique du titre de la carte (h3 quand la carte est nichée sous un sous-titre h2). */
  headingLevel?: "h2" | "h3";
  /** Grise la carte pour signaler un contenu qui n'est plus disponible (ex : événement passé). */
  dimmed?: boolean;
}

export const MediaCard: React.FC<MediaCardProps> = ({
  to,
  imageSrc,
  imageAlt,
  title,
  subtitle,
  excerpt,
  badge,
  headingLevel = "h2",
  dimmed = false,
}) => {
  const Heading = headingLevel;

  return (
    <Link className={`${styles.card}${dimmed ? ` ${styles.dimmed}` : ""}`} to={to}>
      <div className={styles.imageWrapper}>
        <img className={styles.image} src={imageSrc} alt={imageAlt} loading="lazy" />
        {badge && <div className={styles.badge}>{badge}</div>}
      </div>
      <div className={styles.body}>
        {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
        <Heading className={styles.title}>{title}</Heading>
        {excerpt && <p className={styles.excerpt}>{excerpt}</p>}
      </div>
    </Link>
  );
};

export default MediaCard;
