import React from "react";
import { Link } from "react-router-dom";
import Button from "../../components/atoms/Button/Button";
import styles from "./AboutPage.module.css";

const values = [
  {
    title: "Solidarité",
    description: "Chaque collecte, chaque don, chaque bénévole compte pour soutenir les malades et leurs proches.",
  },
  {
    title: "Engagement",
    description: "Des motards mobilisés toute l'année sur le terrain, aux côtés du monde médical et associatif.",
  },
  {
    title: "Proximité",
    description: "Une organisation en secteurs et en groupes locaux, au plus près des besoins du territoire.",
  },
  {
    title: "Convivialité",
    description: "Des événements ouverts à tous pour rassembler motards, familles et sympathisants autour d'une même cause.",
  },
];

const actions = [
  "Organisation de balades et de collectes solidaires",
  "Campagnes de sensibilisation au dépistage",
  "Reversement des fonds récoltés à la recherche contre le cancer",
  "Accompagnement des patients et de leurs proches en lien avec les établissements de santé locaux",
  "Actions de mécénat avec les entreprises du territoire",
];

export const AboutPage: React.FC = () => (
  <div className="container">
    <h1>Qui sommes-nous ?</h1>
    <p className={styles.lead}>
      Une Rose Un Espoir est une association loi 1901 basée à Algrange, née de la volonté d'un groupe de
      motards de mettre leur passion au service de la lutte contre le cancer.
    </p>

    <section className={styles.section}>
      <h2>Notre histoire</h2>
      <p>
        Depuis sa création, l'association réunit des motards du bassin d'Algrange et des environs autour
        d'un objectif commun : soutenir la recherche contre le cancer et accompagner les malades et leurs
        familles. Ce qui a commencé comme une balade solidaire entre passionnés est devenu, au fil des
        années, un mouvement local rassemblant bénévoles, chefs de groupe, partenaires et donateurs.
      </p>
    </section>

    <section className={styles.section}>
      <h2>Nos valeurs</h2>
      <div className={styles.valuesGrid}>
        {values.map((value) => (
          <div className={styles.valueCard} key={value.title}>
            <h3 className={styles.valueTitle}>{value.title}</h3>
            <p className={styles.valueDescription}>{value.description}</p>
          </div>
        ))}
      </div>
    </section>

    <section className={styles.section}>
      <h2>Nos actions</h2>
      <ul className={styles.actionsList}>
        {actions.map((action) => (
          <li key={action}>{action}</li>
        ))}
      </ul>
    </section>

    <section className={styles.ctaSection}>
      <h2>Envie de nous rejoindre ?</h2>
      <p>Que ce soit pour devenir bénévole, adhérer à l'association ou simplement nous soutenir, chaque geste compte.</p>
      <div className={styles.ctaActions}>
        <Link to="/inscription">
          <Button label="Devenir membre" variant="accent" />
        </Link>
        <Link to="/don">
          <Button label="Faire un don" />
        </Link>
      </div>
    </section>
  </div>
);

export default AboutPage;
