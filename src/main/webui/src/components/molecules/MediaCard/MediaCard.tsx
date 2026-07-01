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
}

export const MediaCard: React.FC<MediaCardProps> = ({ to, imageSrc, imageAlt, title, subtitle, excerpt, badge }) => (
  <Link className={styles.card} to={to}>
    <div className={styles.imageWrapper}>
      <img className={styles.image} src={imageSrc} alt={imageAlt} loading="lazy" />
      {badge && <div className={styles.badge}>{badge}</div>}
    </div>
    <div className={styles.body}>
      {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
      <h2 className={styles.title}>{title}</h2>
      {excerpt && <p className={styles.excerpt}>{excerpt}</p>}
    </div>
  </Link>
);

export default MediaCard;
