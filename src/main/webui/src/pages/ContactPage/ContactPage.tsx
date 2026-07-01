import React, { useState } from "react";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import styles from "./ContactPage.module.css";

interface FormState {
  name: string;
  email: string;
  message: string;
}

const initialState: FormState = { name: "", email: "", message: "" };

function validate(values: FormState): Partial<Record<keyof FormState, string>> {
  const errors: Partial<Record<keyof FormState, string>> = {};
  if (!values.name.trim()) errors.name = "Merci d'indiquer votre nom.";
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) errors.email = "Adresse e-mail invalide.";
  if (values.message.trim().length < 10) errors.message = "Votre message doit contenir au moins 10 caractères.";
  return errors;
}

export const ContactPage: React.FC = () => {
  const [values, setValues] = useState<FormState>(initialState);
  const [errors, setErrors] = useState<Partial<Record<keyof FormState, string>>>({});
  const [status, setStatus] = useState<"idle" | "sending" | "sent">("idle");

  const handleChange = (field: keyof FormState) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setValues((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const validationErrors = validate(values);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) return;

    setStatus("sending");
    await new Promise((resolve) => setTimeout(resolve, 600));
    setStatus("sent");
    setValues(initialState);
  };

  if (status === "sent") {
    return (
      <div className="container-narrow">
        <h1>Message envoyé</h1>
        <p>Merci pour votre message, nous vous répondrons dans les meilleurs délais.</p>
      </div>
    );
  }

  return (
    <div className="container-narrow">
      <h1>Contact</h1>
      <p>
        Une question, une envie de rejoindre l'aventure ? Écrivez-nous, ou contactez-nous directement à{" "}
        <a href="mailto:contact@urue-algrange.fr">contact@urue-algrange.fr</a>.
      </p>
      <form className={styles.form} onSubmit={handleSubmit} noValidate>
        <FormField label="Nom" name="name" value={values.name} onChange={handleChange("name")} error={errors.name} required />
        <FormField
          label="E-mail"
          name="email"
          type="email"
          value={values.email}
          onChange={handleChange("email")}
          error={errors.email}
          required
        />
        <FormField
          label="Message"
          name="message"
          multiline
          rows={5}
          value={values.message}
          onChange={handleChange("message")}
          error={errors.message}
          required
        />
        <Button type="submit" label={status === "sending" ? "Envoi..." : "Envoyer"} disabled={status === "sending"} />
      </form>
    </div>
  );
};

export default ContactPage;
