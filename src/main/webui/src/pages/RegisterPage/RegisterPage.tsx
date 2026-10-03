import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import Button from "../../components/atoms/Button/Button";
import SplitPanel from "../../components/organisms/SplitPanel/SplitPanel";
import forms from "../../theme/forms.module.css";

/** Le compte se crée sur le formulaire d'inscription de Keycloak ; on revient ensuite connecté, comme Bénévole. */
export const RegisterPage: React.FC = () => {
  const { register } = useAuth();

  return (
    <SplitPanel
      eyebrow="Rejoindre l'association"
      title="Le cœur des motards"
      aside={
        <p>
          Membre, bénévole ou sympathisant : créez votre compte pour suivre nos actions et vous inscrire aux
          événements.
        </p>
      }
      heading="Inscription"
    >
      <p>Vous allez être redirigé vers le formulaire d'inscription sécurisé de l'association.</p>
      <div className={forms.actions}>
        <Button type="button" label="Créer mon compte" variant="accent" onClick={register} />
      </div>
      <p className={forms.footer}>
        Déjà inscrit ? <Link to="/connexion">Connectez-vous</Link>
      </p>
    </SplitPanel>
  );
};

export default RegisterPage;
