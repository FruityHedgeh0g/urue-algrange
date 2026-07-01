import React from "react";
import styles from "./Footer.module.css";

export const Footer: React.FC = () => (
  <footer className={styles.footer}>
    © {new Date().getFullYear()} Une Rose Un Espoir - Algrange · Association loi 1901
  </footer>
);

export default Footer;
