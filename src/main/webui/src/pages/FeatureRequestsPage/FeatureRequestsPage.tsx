import React, { useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { useCreateFeatureRequest } from "../../features/featureRequests/useFeatureRequests";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import PageHero from "../../components/organisms/PageHero/PageHero";
import styles from "./FeatureRequestsPage.module.css";

export const FeatureRequestsPage: React.FC = () => {
  const { user } = useAuth();
  const createRequest = useCreateFeatureRequest();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !description.trim() || !user) return;
    createRequest.mutate(
      { title, description, requestedBy: `${user.firstName} ${user.lastName}` },
      {
        onSuccess: () => {
          setTitle("");
          setDescription("");
        },
      }
    );
  };

  return (
    <>
      <PageHero
        compact
        eyebrow="Support"
        title="Demandes de fonctionnalités"
        lead="Une idée pour améliorer le site ? Décrivez votre besoin."
      />
      <div className="container">
        <form className={styles.form} onSubmit={handleSubmit} noValidate>
          <FormField label="Titre" value={title} onChange={(e) => setTitle(e.target.value)} required />
          <FormField label="Description" multiline rows={5} value={description} onChange={(e) => setDescription(e.target.value)} required />
          <div>
            <Button type="submit" label="Soumettre la demande" variant="accent" disabled={createRequest.isPending} />
          </div>
        </form>
      </div>
    </>
  );
};

export default FeatureRequestsPage;
