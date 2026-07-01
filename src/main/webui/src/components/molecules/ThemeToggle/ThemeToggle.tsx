import React from "react";
import { useTheme } from "../../../theme/ThemeContext";
import styles from "./ThemeToggle.module.css";

export const ThemeToggle: React.FC = () => {
  const { theme, toggleTheme } = useTheme();
  const isDark = theme === "dark";

  return (
    <button
      type="button"
      className={styles.toggle}
      onClick={toggleTheme}
      aria-pressed={isDark}
      aria-label={isDark ? "Passer en mode clair" : "Passer en mode sombre"}
      title={isDark ? "Mode clair" : "Mode sombre"}
    >
      <span className={styles.icon} aria-hidden>{isDark ? "☀️" : "🌙"}</span>
    </button>
  );
};

export default ThemeToggle;
