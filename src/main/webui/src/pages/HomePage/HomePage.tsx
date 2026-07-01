import React from "react";
import Carousel from "../../components/organisms/Carousel/Carousel";
import Button from "../../components/atoms/Button/Button";
import styles from "./HomePage.module.css";

const base = import.meta.env.BASE_URL;

const slides = [
  {
    src: `${base}logo_asso_transparent.png`,
    alt: "Une Rose Un Espoir Algrange",
    title: "Bienvenue à Une Rose Un Espoir - Algrange",
    caption: "Ensemble contre le cancer",
  },
  {
    src: `${base}logo_asso_transparent.png`,
    alt: "Collecte 2025",
    title: "Collecte 2025",
    caption: "Rejoignez l'aventure et devenez bénévole.",
    href: "#benevolat",
  },
  {
    src: `${base}logo_asso_transparent.png`,
    alt: "Nos événements",
    title: "Actualités & Événements",
    caption: "Suivez nos dernières publications et actions.",
    href: "#events",
  },
];

export const HomePage: React.FC = () => (
  <>
    <Carousel slides={slides} interval={4000} />

    <section id="about" className={styles.section}>
      <h2>Qui sommes-nous ?</h2>
      <p>
        Une Rose Un Espoir est une association loi 1901 basée à Algrange, dédiée à la lutte
        contre le cancer du sein. Nous soutenons les patients, leurs familles, et participons
        activement aux campagnes de sensibilisation et de collecte de fonds pour la recherche.
      </p>
      <Button label="En savoir plus" />
    </section>

    <div className={styles.sectionAlt}>
      <section id="don" className={styles.section}>
        <h2>Faire un don</h2>
        <p>
          Votre soutien est essentiel. Chaque don contribue directement au financement de la
          recherche et à l'accompagnement des malades et de leurs proches.
        </p>
        <Button label="Faire un don" variant="accent" />
      </section>
    </div>

    <section id="benevolat" className={styles.section}>
      <h2>Devenir bénévole</h2>
      <p>
        Rejoignez notre équipe de bénévoles engagés. Que ce soit pour les collectes, les
        événements ou la communication, votre aide compte énormément.
      </p>
      <Button label="Je m'engage" />
    </section>

    <div className={styles.sectionAlt}>
      <section id="events" className={styles.section}>
        <h2>Événements à venir</h2>
        <p className={styles.muted}>Aucun événement planifié pour le moment. Revenez bientôt !</p>
      </section>
    </div>

    <section id="contact" className={styles.section}>
      <h2>Contact</h2>
      <p>
        Pour nous contacter :{" "}
        <a href="mailto:contact@urue-algrange.fr">contact@urue-algrange.fr</a>
      </p>
    </section>
  </>
);

export default HomePage;
