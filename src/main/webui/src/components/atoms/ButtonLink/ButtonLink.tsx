import React from "react";
import { Link } from "react-router-dom";
import styles from "../Button/Button.module.css";
import Icon from "../Icon/Icon";
import { ButtonVariant } from "../Button/Button";

export interface ButtonLinkProps {
  to: string;
  label: string;
  variant?: ButtonVariant;
  /** Flèche après le libellé (appel à l'action). */
  arrow?: boolean;
  className?: string;
}

/** Lien de navigation avec l'apparence d'un bouton (évite d'imbriquer un <button> dans un <a>). */
export const ButtonLink: React.FC<ButtonLinkProps> = ({ to, label, variant = "primary", arrow = false, className }) => (
  <Link className={`${styles.button} ${styles[variant]}${className ? ` ${className}` : ""}`} to={to}>
    <span className={styles.content}>
      <span className={styles.label}>{label}</span>
      {arrow && <Icon name="arrowRight" size={18} />}
    </span>
  </Link>
);

export default ButtonLink;
