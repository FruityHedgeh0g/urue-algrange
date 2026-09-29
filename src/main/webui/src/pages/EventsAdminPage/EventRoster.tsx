import React from "react";
import { useRoster } from "../../features/events/useRoster";
import { RosterEntry } from "../../features/events/registrationsApi";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./EventRoster.module.css";

interface EventRosterProps {
  eventId: string;
  /** false une fois l'Événement archivé : la liste est figée. */
  editable: boolean;
}

/**
 * Participants et Liste d'attente d'un Événement, dans l'ordre d'inscription.
 * Le Bureau fait monter à la main (tant que le maximum n'est pas atteint) et
 * retire des personnes ; personne ne monte automatiquement.
 */
export const EventRoster: React.FC<EventRosterProps> = ({ eventId, editable }) => {
  const { roster, moveUp, removePerson } = useRoster(eventId);
  const pending = moveUp.isPending || removePerson.isPending;

  if (roster.isLoading) return <Spinner label="Chargement des inscrits..." />;
  if (!roster.data) return null;

  const { participants, waiting, maxParticipants } = roster.data;
  const full = maxParticipants !== null && participants.length >= maxParticipants;
  const error = moveUp.error ?? removePerson.error;

  const row = (entry: RosterEntry, action: React.ReactNode) => (
    <li key={entry.personId} className={styles.row}>
      <span className={styles.person}>
        {entry.firstName} {entry.lastName}
        <span className={styles.phone}>{entry.phone}</span>
      </span>
      {editable && action}
    </li>
  );

  return (
    <section className={styles.roster} aria-label="Inscrits">
      <h3 className={styles.title}>
        Participants ({participants.length}
        {maxParticipants !== null ? ` / ${maxParticipants}` : ""})
      </h3>
      {participants.length === 0 ? (
        <p className={styles.empty}>Aucun participant.</p>
      ) : (
        <ul className={styles.list} aria-label="Participants">
          {participants.map((p) =>
            row(
              p,
              <Button
                type="button"
                label="Retirer"
                variant="outline"
                disabled={pending}
                onClick={() => removePerson.mutate(p.personId)}
              />
            )
          )}
        </ul>
      )}

      <h3 className={styles.title}>Liste d'attente ({waiting.length})</h3>
      {waiting.length === 0 ? (
        <p className={styles.empty}>Personne en attente.</p>
      ) : (
        <ul className={styles.list} aria-label="Liste d'attente">
          {waiting.map((w) =>
            row(
              w,
              <span className={styles.actions}>
                <Button
                  type="button"
                  label="Faire monter"
                  disabled={pending || full}
                  onClick={() => moveUp.mutate(w.personId)}
                />
                <Button
                  type="button"
                  label="Retirer"
                  variant="outline"
                  disabled={pending}
                  onClick={() => removePerson.mutate(w.personId)}
                />
              </span>
            )
          )}
        </ul>
      )}
      {full && waiting.length > 0 && editable && <p className={styles.empty}>Maximum atteint : libérez une place pour faire monter quelqu'un.</p>}
      {error && <p className={styles.error}>{error.message}</p>}
    </section>
  );
};

export default EventRoster;
