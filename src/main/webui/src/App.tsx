import React from "react";
import Navbar from "./components/Navbar";
import Carousel from "./components/Carousel";
import ClickableButton from "./components/ClickableButton";

const slides = [
  {
    src: "/logo_asso_transparent.png",
    alt: "Une Rose Un Espoir Algrange",
    title: "Bienvenue à Une Rose Un Espoir - Algrange",
    caption: "Ensemble contre le cancer",
  },
  {
    src: "/vite.svg",
    alt: "Collecte 2025",
    title: "Collecte 2025",
    caption: "Rejoignez l'aventure et devenez bénévole.",
    href: "#benevolat",
  },
  {
    src: "/vite.svg",
    alt: "Nos événements",
    title: "Actualités & Événements",
    caption: "Suivez nos dernières publications et actions.",
    href: "#events",
  },
];

const sectionBase: React.CSSProperties = {
  padding: "64px 24px",
  maxWidth: 960,
  margin: "0 auto",
};

const sectionAlt: React.CSSProperties = {
  background: "var(--color-surface)",
  borderTop: "1px solid var(--color-border)",
  borderBottom: "1px solid var(--color-border)",
};

function App() {
  return (
    <>
      <Navbar />
      <Carousel slides={slides} interval={4000} />

      {/* À propos */}
      <section id="about" style={sectionBase}>
        <h2>Qui sommes-nous ?</h2>
        <p>
          Une Rose Un Espoir est une association loi 1901 basée à Algrange, dédiée à la lutte
          contre le cancer du sein. Nous soutenons les patients, leurs familles, et participons
          activement aux campagnes de sensibilisation et de collecte de fonds pour la recherche.
        </p>
        <ClickableButton label="En savoir plus" onClick={() => {}} />
      </section>

      {/* Collecte / Dons */}
      <div style={sectionAlt}>
        <section id="don" style={sectionBase}>
          <h2>Faire un don</h2>
          <p>
            Votre soutien est essentiel. Chaque don contribue directement au financement de la
            recherche et à l'accompagnement des malades et de leurs proches.
          </p>
          <ClickableButton label="Faire un don" onClick={() => {}} />
        </section>
      </div>

      {/* Bénévolat */}
      <section id="benevolat" style={sectionBase}>
        <h2>Devenir bénévole</h2>
        <p>
          Rejoignez notre équipe de bénévoles engagés. Que ce soit pour les collectes, les
          événements ou la communication, votre aide compte énormément.
        </p>
        <ClickableButton label="Je m'engage" onClick={() => {}} />
      </section>

      {/* Événements */}
      <div style={sectionAlt}>
        <section id="events" style={sectionBase}>
          <h2>Événements à venir</h2>
          <p style={{ color: "var(--color-muted)" }}>
            Aucun événement planifié pour le moment. Revenez bientôt !
          </p>
        </section>
      </div>

      {/* Contact */}
      <section id="contact" style={sectionBase}>
        <h2>Contact</h2>
        <p>
          Pour nous contacter :{" "}
          <a href="mailto:contact@urue-algrange.fr">contact@urue-algrange.fr</a>
        </p>
      </section>

      {/* Footer */}
      <footer
        style={{
          background: "var(--color-surface)",
          borderTop: "1px solid var(--color-border)",
          padding: "24px",
          textAlign: "center",
          color: "var(--color-muted)",
          fontSize: 14,
        }}
      >
        © {new Date().getFullYear()} Une Rose Un Espoir - Algrange · Association loi 1901
      </footer>
    </>
  );
}

export default App;
