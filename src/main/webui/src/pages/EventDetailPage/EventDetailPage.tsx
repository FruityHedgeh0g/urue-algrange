import React from "react";
import { Link, useParams } from "react-router-dom";
import { useEvent } from "../../features/events/useEvents";
import { isUpcoming } from "../../features/events/eventsApi";
import { useMyEventIds, useEventRegistration } from "../../features/events/useMyRegistrations";
import { useAuth } from "../../auth/AuthContext";
import { formatDateRange } from "../../lib/formatDate";
import Spinner from "../../components/atoms/Spinner/Spinner";
import Badge from "../../components/atoms/Badge/Badge";
import Button from "../../components/atoms/Button/Button";
import styles from "./EventDetailPage.module.css";

export const EventDetailPage: React.FC = () => {
  const { eventId } = useParams<{ eventId: string }>();
  const { data: event, isLoading, isError } = useEvent(eventId);
  const { isAuthenticated } = useAuth();
  const { data: myEventIds } = useMyEventIds();
  const { register, unregister } = useEventRegistration();

  const isRegistered = Boolean(eventId && myEventIds?.includes(eventId));
  const isPending = register.isPending || unregister.isPending;

  return (
    <div className="container">
      <Link className={styles.back} to="/evenements">
        ← Retour aux événements
      </Link>

      {isLoading && <Spinner label="Chargement de l'événement..." />}
      {isError && <p className={styles.error}>Impossible de charger cet événement.</p>}
      {!isLoading && !isError && !event && <p className={styles.error}>Cet événement n'existe pas.</p>}

      {event && (
        <article>
          <Badge label={isUpcoming(event) ? "À venir" : "Terminé"} tone={isUpcoming(event) ? "accent" : "muted"} />
          <h1>{event.name}</h1>
          <p className={styles.meta}>{formatDateRange(event.startDateTime, event.endDateTime)}</p>
          {event.address && (
            <p className={styles.meta}>
              {event.address}, {event.postalCode} {event.city}
            </p>
          )}
          <p className={styles.description}>{event.description}</p>

          {isUpcoming(event) && (
            <div className={styles.actions}>
              {isAuthenticated ? (
                isRegistered ? (
                  <Button
                    label="Me désinscrire"
                    variant="outline"
                    disabled={isPending}
                    onClick={() => unregister.mutate(event.eventId)}
                  />
                ) : (
                  <Button
                    label="M'inscrire à cet événement"
                    variant="accent"
                    disabled={isPending}
                    onClick={() => register.mutate(event.eventId)}
                  />
                )
              ) : (
                <Link to="/connexion">
                  <Button label="Se connecter pour m'inscrire" />
                </Link>
              )}
            </div>
          )}
        </article>
      )}
    </div>
  );
};

export default EventDetailPage;
