import React from "react";
import { useGroupActions, useMonGroupe } from "../../features/events/useRoster";
import { RosterEntry } from "../../features/events/registrationsApi";
import { EVENT_STATUS_LABELS } from "../../features/events/status";
import { formatDate } from "../../lib/formatDate";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./MonGroupePage.module.css";

/**
 * Mon groupe : pour le Groupe que la personne mène (Affectation), la liste de
 * chaque Événement — membres et Demandes de groupe en attente — avec les
 * actions du Chef : accepter, refuser, sortir du groupe.
 */
export const MonGroupePage: React.FC = () => {
  const { data, isLoading } = useMonGroupe();
  const { decide, takeOut } = useGroupActions();
  const pending = decide.isPending || takeOut.isPending;
  const error = decide.error ?? takeOut.error;

  if (isLoading) return <Spinner label="Chargement de votre groupe..." />;
  if (!data?.group) return <p className={styles.empty}>Vous ne menez aucun groupe pour le moment.</p>;

  const person = (entry: RosterEntry, actions: React.ReactNode) => (
    <li key={entry.personId} className={styles.row}>
      <span className={styles.person}>
        {entry.firstName} {entry.lastName}
        <span className={styles.phone}>{entry.phone}</span>
      </span>
      <span className={styles.actions}>{actions}</span>
    </li>
  );

  return (
    <div className={styles.page}>
      <h2 className={styles.title}>{data.group.name}</h2>
      {data.events.length === 0 && <p className={styles.empty}>Aucun événement ne concerne votre groupe pour le moment.</p>}
      {error && <p className={styles.error}>{error.message}</p>}

      {data.events.map((event) => (
        <section key={event.eventId} className={styles.event} aria-label={event.name}>
          <h3 className={styles.eventTitle}>
            {event.name}
            <span className={styles.meta}>
              {formatDate(event.startDateTime)} · {EVENT_STATUS_LABELS[event.status]}
            </span>
          </h3>

          {event.members.length === 0 ? (
            <p className={styles.empty}>Aucun membre pour cet événement.</p>
          ) : (
            <ul className={styles.list} aria-label="Membres du groupe">
              {event.members.map((m) =>
                person(
                  m,
                  <Button
                    type="button"
                    label="Sortir du groupe"
                    variant="outline"
                    disabled={pending}
                    onClick={() => takeOut.mutate({ eventId: event.eventId, personId: m.personId })}
                  />
                )
              )}
            </ul>
          )}

          {event.demandes.length > 0 && (
            <ul className={styles.list} aria-label="Demandes en attente">
              {event.demandes.map((d) =>
                person(
                  d,
                  <>
                    <Button
                      type="button"
                      label="Accepter"
                      disabled={pending}
                      onClick={() => decide.mutate({ eventId: event.eventId, personId: d.personId, accept: true })}
                    />
                    <Button
                      type="button"
                      label="Refuser"
                      variant="outline"
                      disabled={pending}
                      onClick={() => decide.mutate({ eventId: event.eventId, personId: d.personId, accept: false })}
                    />
                  </>
                )
              )}
            </ul>
          )}
        </section>
      ))}
    </div>
  );
};

export default MonGroupePage;
