import React, { useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { ROLE_LABELS } from "../../auth/roles";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Badge from "../../components/atoms/Badge/Badge";
import Icon from "../../components/atoms/Icon/Icon";
import styles from "./ProfilePage.module.css";

export const ProfilePage: React.FC = () => {
  const { user, updateProfile } = useAuth();
  const [firstName, setFirstName] = useState(user?.firstName ?? "");
  const [lastName, setLastName] = useState(user?.lastName ?? "");
  const [phone, setPhone] = useState(user?.phone ?? "");
  const [saved, setSaved] = useState(false);

  if (!user) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateProfile({ firstName, lastName, phone: phone.trim() });
    setSaved(true);
  };

  const initials = `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`.toUpperCase();

  return (
    <div className={styles.layout}>
      <aside className={styles.identity}>
        <span className={styles.avatar} aria-hidden="true">
          {initials}
        </span>
        <p className={styles.name}>
          {user.firstName} {user.lastName}
        </p>
        <div className={styles.badges}>
          <Badge label={ROLE_LABELS[user.role]} />
        </div>
      </aside>

      <section className={styles.panel}>
        <p className="eyebrow">Informations personnelles</p>
        <form className={styles.form} onSubmit={handleSubmit} noValidate>
          <div className={styles.row}>
            <FormField
              label="Prénom"
              value={firstName}
              onChange={(e) => {
                setFirstName(e.target.value);
                setSaved(false);
              }}
              required
            />
            <FormField
              label="Nom"
              value={lastName}
              onChange={(e) => {
                setLastName(e.target.value);
                setSaved(false);
              }}
              required
            />
          </div>
          <FormField
            label="Téléphone"
            type="tel"
            autoComplete="tel"
            value={phone}
            onChange={(e) => {
              setPhone(e.target.value);
              setSaved(false);
            }}
          />
          <p className={styles.hint}>Nécessaire pour vous inscrire à un événement.</p>
          <div className={styles.actions}>
            <Button type="submit" label="Enregistrer" />
            {saved && (
              <p className={styles.saved}>
                <Icon name="check" size={18} strokeWidth={3} />
                Vos informations ont été mises à jour.
              </p>
            )}
          </div>
        </form>
      </section>
    </div>
  );
};

export default ProfilePage;
