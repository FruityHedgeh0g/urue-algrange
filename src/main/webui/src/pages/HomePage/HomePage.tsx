import React from "react";
import { Link } from "react-router-dom";
import Carousel from "../../components/organisms/Carousel/Carousel";
import EventList from "../../components/organisms/EventList/EventList";
import SectionTeaser from "../../components/molecules/SectionTeaser/SectionTeaser";
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
    to: "/#benevolat",
  },
  {
    src: placeholderImage("home-slide-events", "Actualités & Événements"),
    alt: "Nos événements",
    title: "Actualités & Événements",
    caption: "Suivez nos dernières publications et actions.",
    to: "/evenements",
  },
];

export const HomePage: React.FC = () => (
  <>
    <h1 className="sr-only">Une Rose Un Espoir - Algrange</h1>
    <Carousel slides={slides} interval={4000} />

    <SectionTeaser
      id="about"
      title="Qui sommes-nous ?"
      description="Une Rose Un Espoir est une association loi 1901 basée à Algrange, dédiée à la lutte contre le cancer du sein. Nous soutenons les patients, leurs familles, et participons activement aux campagnes de sensibilisation et de collecte de fonds pour la recherche."
      to="/qui-sommes-nous"
      linkLabel="En savoir plus"
    />

    <div className={styles.sectionAlt}>
      <SectionTeaser
        id="don"
        title="Faire un don"
        description="Votre soutien est essentiel. Chaque don contribue directement au financement de la recherche et à l'accompagnement des malades et de leurs proches."
        to="/don"
        linkLabel="Faire un don"
      />
    </div>

    <SectionTeaser
      id="benevolat"
      title="Devenir bénévole"
      description="Rejoignez notre équipe de bénévoles engagés. Que ce soit pour les collectes, les événements ou la communication, votre aide compte énormément."
      to="/inscription"
      linkLabel="Je m'engage"
    />

    <div className={styles.sectionAlt}>
      <section id="events" className={styles.section}>
        <h2>Événements à venir</h2>
        <EventList scope="upcoming" limit={3} />
        <Link className={styles.seeAll} to="/evenements">
          Voir tous les événements →
        </Link>
      </section>
    </div>

    <SectionTeaser
      id="contact"
      title="Contact"
      description="Une question, une envie de rejoindre l'aventure ? Nous serions ravis de vous répondre."
      to="/contact"
      linkLabel="Nous contacter"
    />
  </>
);

export default HomePage;
