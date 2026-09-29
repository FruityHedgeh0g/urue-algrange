import React from "react";
import { Link, useParams } from "react-router-dom";
import { useEvent } from "../../features/events/useEvents";
import { acceptsSignUps, EVENT_STATUS_LABELS, statusTone } from "../../features/events/status";
import { useMyEventIds, useEventRegistration } from "../../features/events/useMyRegistrations";
import { useFeature } from "../../features/featureFlags/useFeatureFlags";
import { useAuth } from "../../auth/AuthContext";
import { formatDateRange } from "../../lib/formatDate";
import { placeholderImage } from "../../lib/placeholderImage";
import PageHero from "../../components/organisms/PageHero/PageHero";
import Spinner from "../../components/atoms/Spinner/Spinner";
import Badge from "../../components/atoms/Badge/Badge";
import Button from "../../components/atoms/Button/Button";
import ButtonLink from "../../components/atoms/ButtonLink/ButtonLink";
import Icon from "../../components/atoms/Icon/Icon";
import styles from "./EventDetailPage.module.css";

export const EventDetailPage: React.FC = () => {
  const { eventId } = useParams<{ eventId: string }>();
  const { data: event, isLoading, isError } = useEvent(eventId);
  const { isAuthenticated } = useAuth();
  const { data: myEventIds } = useMyEventIds();
  const { register, unregister } = useEventRegistration();
  const registrationOpen = useFeature("inscription-evenements");

  const isRegistered = Boolean(eventId && myEventIds?.includes(eventId));
  const isPending = register.isPending || unregister.isPending;

  const back = (
    <Link className={styles.back} to="/evenements">
      <Icon name="arrowLeft" size={16} strokeWidth={2.5} /> Retour aux événements
    </Link>
  );

  if (!event) {
    return (
      <div className="container">
        {back}
        {isLoading && <Spinner label="Chargement de l'événement..." />}
        {isError && <p className={styles.error}>Impossible de charger cet événement.</p>}
        {!isLoading && !isError && <p className={styles.error}>Cet événement n'existe pas.</p>}
      </div>
    );
  }

  const signUpsOpen = acceptsSignUps(event.status);

  return (
    <article>
      <PageHero eyebrow="Événement" title={event.name} imageSrc={event.imageUrl || undefined}>
        <Badge label={EVENT_STATUS_LABELS[event.status]} tone={statusTone(event.status)} />
        <span className={styles.heroMeta}>
          <Icon name="calendar" size={18} />
          {formatDateRange(event.startDateTime, event.endDateTime)}
        </span>
      </PageHero>

      <div className="container">
        {back}
        <div className={styles.layout}>
          <div>
            <img
              className={styles.image}
              src={event.imageUrl || placeholderImage(event.eventId, event.name)}
              alt={event.name}
            />
            <p className={styles.description}>{event.description}</p>
          </div>

          <aside className={styles.card}>
            <p className="eyebrow">Infos pratiques</p>
            <ul className={styles.facts}>
              <li>
                <Icon name="calendar" size={20} />
                <span>{formatDateRange(event.startDateTime, event.endDateTime)}</span>
              </li>
              {event.address && (
                <li>
                  <Icon name="pin" size={20} />
                  <span>
                    {event.address}, {event.postalCode} {event.city}
                  </span>
                </li>
              )}
            </ul>

            {signUpsOpen && registrationOpen && (
              <div className={styles.actions}>
                {isAuthenticated ? (
                  isRegistered ? (
                    <>
                      <p className={styles.registered}>
                        <Icon name="check" size={18} strokeWidth={3} /> Vous êtes inscrit
                      </p>
                      <Button
                        label="Me désinscrire"
                        variant="outline"
                        disabled={isPending}
                        onClick={() => unregister.mutate(event.eventId)}
                      />
                    </>
                  ) : (
                    <Button
                      label="M'inscrire à cet événement"
                      variant="accent"
                      disabled={isPending}
                      onClick={() => register.mutate(event.eventId)}
                    />
                  )
                ) : (
                  <ButtonLink to="/connexion" label="Se connecter pour m'inscrire" />
                )}
              </div>
            )}
          </aside>
        </div>
      </div>
    </article>
  );
};

export default EventDetailPage;
