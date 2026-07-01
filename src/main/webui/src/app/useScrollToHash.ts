import { useEffect } from "react";
import { useLocation } from "react-router-dom";

/** Fait défiler vers l'ancre ciblée par l'URL après chaque navigation (les liens de nav pointent vers des sections de la page d'accueil). */
export function useScrollToHash() {
  const { pathname, hash } = useLocation();

  useEffect(() => {
    if (!hash) return;
    const id = hash.slice(1);
    const el = document.getElementById(id);
    if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
  }, [pathname, hash]);
}
