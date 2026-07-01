import React from "react";
import styles from "./Spinner.module.css";

export const Spinner: React.FC<{ label?: string }> = ({ label = "Chargement..." }) => (
  <div className={styles.wrapper} role="status" aria-live="polite">
    <span className={styles.spinner} aria-hidden />
    <span className={styles.label}>{label}</span>
  </div>
);

export default Spinner;
