import React from "react";
import styles from "./Button.module.css";

export type ButtonVariant = "primary" | "accent" | "outline" | "ghost";

export interface ButtonProps {
  /** Texte du bouton */
  label: string;
  /** Callback au clic (optionnel) */
  onClick?: () => void;
  /** Type HTML natif (utile dans un formulaire) */
  type?: "button" | "submit" | "reset";
  /** Variante visuelle */
  variant?: ButtonVariant;
  /** URL de l'image optionnelle à afficher à gauche du label */
  imageSrc?: string;
  /** Texte alternatif de l'image (accessibilité) */
  imageAlt?: string;
  /** Désactiver le bouton (optionnel) */
  disabled?: boolean;
}

export const Button: React.FC<ButtonProps> = ({
  label,
  onClick,
  type = "button",
  variant = "primary",
  imageSrc,
  imageAlt = "",
  disabled = false,
}) => {
  return (
    <button
      type={type}
      className={`${styles.button} ${styles[variant]}`}
      onClick={onClick}
      disabled={disabled}
      aria-label={label}
    >
      <span className={styles.content}>
        {imageSrc && (
          <img
            className={styles.icon}
            src={imageSrc}
            alt={imageAlt}
            aria-hidden={imageAlt ? undefined : true}
          />
        )}
        <span className={styles.label}>{label}</span>
      </span>
    </button>
  );
};

export default Button;
