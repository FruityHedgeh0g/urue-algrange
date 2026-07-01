import React from "react";
import { Link } from "react-router-dom";
import Button from "../../components/atoms/Button/Button";

export const DonationPage: React.FC = () => (
  <div className="container-narrow">
    <h1>Faire un don</h1>
    <p>
      Une Rose Un Espoir agit grâce à la générosité de ses donateurs et partenaires. Chaque don, quel
      que soit son montant, contribue directement au financement de la recherche contre le cancer et à
      l'accompagnement des malades et de leurs proches.
    </p>
    <p>
      L'association est éligible à la réduction d'impôt pour les dons aux associations d'intérêt général.
      Un reçu fiscal vous est adressé pour tout don.
    </p>
    <h2>Mécénat &amp; partenariats entreprises</h2>
    <p>
      Vous représentez une entreprise et souhaitez soutenir nos actions ? Contactez-nous pour échanger sur
      les modalités de mécénat.
    </p>
    <Link to="/contact">
      <Button label="Nous contacter" variant="accent" />
    </Link>
  </div>
);

export default DonationPage;
