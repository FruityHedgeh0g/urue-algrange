import React from "react";
import { useTheme } from "../../../theme/ThemeContext";
import Icon from "../../atoms/Icon/Icon";
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
      <Icon name={isDark ? "sun" : "moon"} size={16} />
    </button>
  );
};

export default ThemeToggle;
