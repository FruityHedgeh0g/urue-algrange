import React, { useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { ROLE_LABELS } from "../../auth/roles";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Badge from "../../components/atoms/Badge/Badge";
import styles from "./ProfilePage.module.css";

export const ProfilePage: React.FC = () => {
  const { user, updateProfile } = useAuth();
  const [firstName, setFirstName] = useState(user?.firstName ?? "");
  const [lastName, setLastName] = useState(user?.lastName ?? "");
  const [saved, setSaved] = useState(false);

  if (!user) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateProfile({ firstName, lastName });
    setSaved(true);
  };

  return (
    <div>
      <div className={styles.badges}>
        <Badge label={ROLE_LABELS[user.role]} />
        <Badge label={user.group.name} tone="muted" />
        <Badge label={user.group.sectorName} tone="muted" />
      </div>

      <form className={styles.form} onSubmit={handleSubmit} noValidate>
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
        <Button type="submit" label="Enregistrer" />
        {saved && <p className={styles.saved}>Vos informations ont été mises à jour.</p>}
      </form>
    </div>
  );
};

export default ProfilePage;
