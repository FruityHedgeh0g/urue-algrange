import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import SplitPanel from "../../components/organisms/SplitPanel/SplitPanel";
import forms from "../../theme/forms.module.css";

interface FormState {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

const initialState: FormState = { firstName: "", lastName: "", email: "", password: "" };

function validate(values: FormState): Partial<Record<keyof FormState, string>> {
  const errors: Partial<Record<keyof FormState, string>> = {};
  if (!values.firstName.trim()) errors.firstName = "Prénom requis.";
  if (!values.lastName.trim()) errors.lastName = "Nom requis.";
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) errors.email = "Adresse e-mail invalide.";
  if (values.password.length < 8) errors.password = "8 caractères minimum.";
  return errors;
}

export const RegisterPage: React.FC = () => {
  const [values, setValues] = useState<FormState>(initialState);
  const [errors, setErrors] = useState<Partial<Record<keyof FormState, string>>>({});
  const { setRole } = useAuth();
  const navigate = useNavigate();

  const handleChange = (field: keyof FormState) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setValues((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const validationErrors = validate(values);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) return;
    setRole("benevole");
    navigate("/");
  };

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
      <form className={forms.form} onSubmit={handleSubmit} noValidate>
        <div className={forms.row}>
          <FormField label="Prénom" value={values.firstName} onChange={handleChange("firstName")} error={errors.firstName} required />
          <FormField label="Nom" value={values.lastName} onChange={handleChange("lastName")} error={errors.lastName} required />
        </div>
        <FormField
          label="E-mail"
          type="email"
          value={values.email}
          onChange={handleChange("email")}
          error={errors.email}
          required
        />
        <FormField
          label="Mot de passe"
          type="password"
          value={values.password}
          onChange={handleChange("password")}
          error={errors.password}
          required
        />
        <div className={forms.actions}>
          <Button type="submit" label="Créer mon compte" variant="accent" />
        </div>
      </form>
      <p className={forms.footer}>
        Déjà inscrit ? <Link to="/connexion">Connectez-vous</Link>
      </p>
    </SplitPanel>
  );
};

export default RegisterPage;
