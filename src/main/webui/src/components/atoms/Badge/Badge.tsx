import React from "react";
import styles from "./Badge.module.css";

export type BadgeTone = "primary" | "accent" | "muted";

export const Badge: React.FC<{ label: string; tone?: BadgeTone }> = ({ label, tone = "primary" }) => (
  <span className={`${styles.badge} ${styles[tone]}`}>{label}</span>
);

export default Badge;
