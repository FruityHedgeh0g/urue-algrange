import React from "react";
import "./ClickableBoutton.css";

export interface ClickableBouttonProps {
    /** Texte du bouton */
    label: string;
    /** Callback au clic (optionnel) */
    onClick?: () => void;
    /** URL de l'image optionnelle à afficher à gauche du label */
    imageSrc?: string;
    /** Texte alternatif de l'image (accessibilité) */
    imageAlt?: string;
    /** Désactiver le bouton (optionnel) */
    disabled?: boolean;
}

const ClickableBoutton: React.FC<ClickableBouttonProps> = ({
                                                               label,
                                                               onClick,
                                                               imageSrc,
                                                               imageAlt = "",
                                                               disabled = false,
                                                           }) => {
    return (
        <button
            type="button"
            className="cb-button"
            onClick={onClick}
            disabled={disabled}
            aria-label={label}
        >
      <span className="cb-content">
        {imageSrc && (
            <img
                className="cb-icon"
                src={imageSrc}
                alt={imageAlt}
                aria-hidden={imageAlt ? undefined : true}
            />
        )}
          <span className="cb-label">{label}</span>
      </span>
        </button>
    );
};

export default ClickableBoutton;
