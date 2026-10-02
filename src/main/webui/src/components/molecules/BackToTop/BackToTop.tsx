import React, { useEffect, useState } from "react";
import Icon from "../../atoms/Icon/Icon";
import styles from "./BackToTop.module.css";

/** Bouton rond "retour en haut" de la charte, visible après un peu de défilement. */
export const BackToTop: React.FC = () => {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const onScroll = () => setVisible(window.scrollY > 600);
    onScroll();
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
    <button
      type="button"
      className={`${styles.button}${visible ? ` ${styles.visible}` : ""}`}
      aria-label="Revenir en haut de la page"
      tabIndex={visible ? 0 : -1}
      onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
    >
      <Icon name="arrowUp" size={18} strokeWidth={2.5} />
    </button>
  );
};

export default BackToTop;
