import React, { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useEvent } from "../../features/events/useEvents";
import { acceptsSignUps, EVENT_STATUS_LABELS, statusTone } from "../../features/events/status";
import { useEventRegistration, useMyRegistrations } from "../../features/events/useMyRegistrations";
import { PhoneRequiredError } from "../../features/events/registrationsApi";
import { useFeature } from "../../features/featureFlags/useFeatureFlags";
import { useAuth } from "../../auth/AuthContext";
import { formatDateRange } from "../../lib/formatDate";
import { placeholderImage } from "../../lib/placeholderImage";
import PageHero from "../../components/organisms/PageHero/PageHero";
import Spinner from "../../components/atoms/Spinner/Spinner";
import Badge from "../../components/atoms/Badge/Badge";
import Button from "../../components/atoms/Button/Button";
import FormField from "../../components/molecules/FormField/FormField";
import ButtonLink from "../../components/atoms/ButtonLink/ButtonLink";
import Icon from "../../components/atoms/Icon/Icon";
import styles from "./EventDetailPage.module.css";

export const EventDetailPage: React.FC = () => {
  const { eventId } = useParams<{ eventId: string }>();
  const { data: event, isLoading, isError } = useEvent(eventId);
  const { isAuthenticated, user, updateProfile } = useAuth();
  const { data: registrations } = useMyRegistrations();
  const { register, unregister } = useEventRegistration();
  const registrationOpen = useFeature("inscription-evenements");
  const [askPhone, setAskPhone] = useState(false);
  const [phone, setPhone] = useState("");

  const registration = registrations?.find((r) => r.eventId === eventId);
  const isPending = register.isPending || unregister.isPending;

  /** Sans téléphone, l'API refuse l'inscription : on le demande, on l'enregistre au profil, puis on réessaie. */
  const signUp = (withPhone?: string) => {
    if (!eventId) return;
    register.mutate(
      { eventId, phone: withPhone },
      {
        onSuccess: () => setAskPhone(false),
        onError: (error) => setAskPhone(error instanceof PhoneRequiredError),
      }
    );
  };

  const savePhoneAndSignUp = (e: React.FormEvent) => {
    e.preventDefault();
    if (!user || !phone.trim()) return;
    updateProfile({ firstName: user.firstName, lastName: user.lastName, phone: phone.trim() });
    signUp(phone.trim());
  };

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

            {registration ? (
              <div className={styles.actions}>
                <p className={styles.registered}>
                  <Icon name="check" size={18} strokeWidth={3} />
                  {registration.status === "participant" ? "Vous êtes inscrit comme pilote" : "Vous êtes sur la liste d'attente"}
                </p>
                {event.status !== "archive" && (
                  <Button
                    label="Me désinscrire"
                    variant="outline"
                    disabled={isPending}
                    onClick={() => unregister.mutate(event.eventId)}
                  />
                )}
              </div>
            ) : (
              signUpsOpen &&
              registrationOpen && (
                <div className={styles.actions}>
                  {!isAuthenticated ? (
                    <ButtonLink to="/connexion" label="Se connecter pour m'inscrire" />
                  ) : askPhone ? (
                    <form onSubmit={savePhoneAndSignUp} noValidate>
                      <FormField
                        label="Téléphone"
                        type="tel"
                        autoComplete="tel"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                      />
                      <p>Votre numéro est nécessaire pour vous inscrire ; il est enregistré dans votre profil.</p>
                      <Button type="submit" label="Enregistrer et m'inscrire" variant="accent" disabled={isPending} />
                    </form>
                  ) : (
                    <>
                      <Button
                        label={event.status === "complet" ? "Rejoindre la liste d'attente" : "M'inscrire comme pilote"}
                        variant="accent"
                        disabled={isPending}
                        onClick={() => signUp()}
                      />
                      {register.isError && !askPhone && <p className={styles.error}>{register.error.message}</p>}
                    </>
                  )}
                </div>
              )
            )}
          </aside>
        </div>
      </div>
    </article>
  );
};

export default EventDetailPage;
