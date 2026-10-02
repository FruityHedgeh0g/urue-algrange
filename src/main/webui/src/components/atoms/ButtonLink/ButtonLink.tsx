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
  /** Téléchargement d'un fichier servi par l'API : un simple <a download>, hors du routeur. */
  download?: boolean;
}

/** Lien de navigation avec l'apparence d'un bouton (évite d'imbriquer un <button> dans un <a>). */
export const ButtonLink: React.FC<ButtonLinkProps> = ({ to, label, variant = "primary", arrow = false, className, download = false }) => {
  const classes = `${styles.button} ${styles[variant]}${className ? ` ${className}` : ""}`;
  const content = (
    <span className={styles.content}>
      <span className={styles.label}>{label}</span>
      {arrow && <Icon name="arrowRight" size={18} />}
    </span>
  );
  return download ? (
    <a className={classes} href={to} download>
      {content}
    </a>
  ) : (
    <Link className={classes} to={to}>
      {content}
    </Link>
  );
};

export default ButtonLink;
