import React, { useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { useCreateFeatureRequest, useFeatureRequests } from "../../features/featureRequests/useFeatureRequests";
import { formatDate } from "../../lib/formatDate";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./FeatureRequestsPage.module.css";

export const FeatureRequestsPage: React.FC = () => {
  const { user } = useAuth();
  const { data: requests, isLoading } = useFeatureRequests();
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
    <div className={styles.wrapper}>
      <form className={styles.form} onSubmit={handleSubmit} noValidate>
        <FormField label="Titre" value={title} onChange={(e) => setTitle(e.target.value)} required />
        <FormField label="Description" multiline rows={3} value={description} onChange={(e) => setDescription(e.target.value)} required />
        <Button type="submit" label="Soumettre la demande" variant="accent" disabled={createRequest.isPending} />
      </form>

      {isLoading && <Spinner label="Chargement des demandes..." />}
      {requests && (
        <ul className={styles.list}>
          {requests.map((request) => (
            <li key={request.id} className={styles.item}>
              <h3 className={styles.title}>{request.title}</h3>
              <p className={styles.meta}>
                Proposé par {request.requestedBy} le {formatDate(request.createdAt)}
              </p>
              <p>{request.description}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default FeatureRequestsPage;
