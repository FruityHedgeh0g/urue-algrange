import React from "react";
import { Link } from "react-router-dom";
import Carousel from "../../components/organisms/Carousel/Carousel";
import EventList from "../../components/organisms/EventList/EventList";
import Button from "../../components/atoms/Button/Button";
import { placeholderImage } from "../../lib/placeholderImage";
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
    src: placeholderImage("home-slide-benevolat", "Devenir bénévole"),
    alt: "Collecte 2025",
    title: "Collecte 2025",
    caption: "Rejoignez l'aventure et devenez bénévole.",
    href: "#benevolat",
  },
  {
    src: placeholderImage("home-slide-events", "Actualités & Événements"),
    alt: "Nos événements",
    title: "Actualités & Événements",
    caption: "Suivez nos dernières publications et actions.",
    href: "/evenements",
  },
];

export const HomePage: React.FC = () => (
  <>
    <h1 className="sr-only">Une Rose Un Espoir - Algrange</h1>
    <Carousel slides={slides} interval={4000} />

    <section id="about" className={styles.section}>
      <h2>Qui sommes-nous ?</h2>
      <p>
        Une Rose Un Espoir est une association loi 1901 basée à Algrange, dédiée à la lutte
        contre le cancer du sein. Nous soutenons les patients, leurs familles, et participons
        activement aux campagnes de sensibilisation et de collecte de fonds pour la recherche.
      </p>
    </section>

    <div className={styles.sectionAlt}>
      <section id="don" className={styles.section}>
        <h2>Faire un don</h2>
        <p>
          Votre soutien est essentiel. Chaque don contribue directement au financement de la
          recherche et à l'accompagnement des malades et de leurs proches.
        </p>
        <Link to="/don">
          <Button label="Faire un don" variant="accent" />
        </Link>
      </section>
    </div>

    <section id="benevolat" className={styles.section}>
      <h2>Devenir bénévole</h2>
      <p>
        Rejoignez notre équipe de bénévoles engagés. Que ce soit pour les collectes, les
        événements ou la communication, votre aide compte énormément.
      </p>
      <Link to="/inscription">
        <Button label="Je m'engage" />
      </Link>
    </section>

    <div className={styles.sectionAlt}>
      <section id="events" className={styles.section}>
        <h2>Événements à venir</h2>
        <EventList scope="upcoming" limit={3} />
        <Link className={styles.seeAll} to="/evenements">
          Voir tous les événements →
        </Link>
      </section>
    </div>

    <section id="contact" className={styles.section}>
      <h2>Contact</h2>
      <p>Une question, une envie de rejoindre l'aventure ? Nous serions ravis de vous répondre.</p>
      <Link to="/contact">
        <Button label="Nous contacter" />
      </Link>
    </section>
  </>
);

export default HomePage;
