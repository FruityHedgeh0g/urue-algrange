import React, { useState } from "react";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Icon from "../../components/atoms/Icon/Icon";
import SplitPanel from "../../components/organisms/SplitPanel/SplitPanel";
import forms from "../../theme/forms.module.css";
import styles from "./ContactPage.module.css";

interface FormState {
  name: string;
  email: string;
  message: string;
}

const CONTACT_EMAIL = "contact@urue-algrange.fr";
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

  const aside = (
    <ul className={styles.channels}>
      <li>
        <Icon name="mail" size={20} />
        <a href={`mailto:${CONTACT_EMAIL}`}>{CONTACT_EMAIL}</a>
      </li>
      <li>
        <Icon name="pin" size={20} />
        <span>Algrange, Moselle</span>
      </li>
    </ul>
  );

  if (status === "sent") {
    return (
      <SplitPanel eyebrow="Contact" title="Merci !" aside={aside} heading="Message envoyé">
        <p className={styles.sent}>
          <span className={styles.sentIcon} aria-hidden="true">
            <Icon name="check" size={28} strokeWidth={3} />
          </span>
          Merci pour votre message, nous vous répondrons dans les meilleurs délais.
        </p>
      </SplitPanel>
    );
  }

  return (
    <SplitPanel
      eyebrow="Contact"
      title="Parlons-en"
      aside={aside}
      heading="Contact"
      intro={
        <p>
          Une question, une envie de rejoindre l'aventure ? Écrivez-nous, ou contactez-nous directement à{" "}
          <a href={`mailto:${CONTACT_EMAIL}`}>{CONTACT_EMAIL}</a>.
        </p>
      }
    >
      <form className={forms.form} onSubmit={handleSubmit} noValidate>
        <div className={forms.row}>
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
        </div>
        <FormField
          label="Message"
          name="message"
          multiline
          rows={6}
          value={values.message}
          onChange={handleChange("message")}
          error={errors.message}
          required
        />
        <div className={forms.actions}>
          <Button
            type="submit"
            label={status === "sending" ? "Envoi..." : "Envoyer"}
            variant="accent"
            disabled={status === "sending"}
          />
        </div>
      </form>
    </SplitPanel>
  );
};

export default ContactPage;
