import React from "react";
import { Outlet } from "react-router-dom";
import Header from "../../organisms/Header/Header";
import Footer from "../../organisms/Footer/Footer";
import styles from "./PublicLayout.module.css";

export const PublicLayout: React.FC = () => (
  <div className={styles.layout}>
    <Header />
    <main className={styles.main}>
      <Outlet />
    </main>
    <Footer />
  </div>
);

export default PublicLayout;
