import React from "react";
import { useGroupActions, useRoster } from "../../features/events/useRoster";
import { RosterEntry } from "../../features/events/registrationsApi";
import { useGroups } from "../../features/groups/useGroups";
import Button from "../../components/atoms/Button/Button";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./EventRoster.module.css";

interface EventRosterProps {
  eventId: string;
  /** Secteur de l'Événement : ses Groupes sont proposés pour placer les Participants. */
  sectorId: string;
  /** false une fois l'Événement archivé : la liste est figée. */
  editable: boolean;
}

/**
 * Participants et Liste d'attente d'un Événement, dans l'ordre d'inscription.
 * Le Bureau fait monter à la main (tant que le maximum n'est pas atteint),
 * retire des personnes, décide des Demandes de groupe et place ou sort des
 * Participants d'un Groupe ; personne ne monte automatiquement.
 */
export const EventRoster: React.FC<EventRosterProps> = ({ eventId, sectorId, editable }) => {
  const { roster, moveUp, removePerson, place } = useRoster(eventId);
  const { decide, takeOut } = useGroupActions();
  const { data: groups } = useGroups();
  const pending = moveUp.isPending || removePerson.isPending || place.isPending || decide.isPending || takeOut.isPending;

  if (roster.isLoading) return <Spinner label="Chargement des inscrits..." />;
  if (!roster.data) return null;

  const { participants, waiting, maxParticipants } = roster.data;
  const full = maxParticipants !== null && participants.length >= maxParticipants;
  const error = moveUp.error ?? removePerson.error ?? place.error ?? decide.error ?? takeOut.error;
  const groupOptions = [
    { value: "", label: "Sans groupe" },
    ...(groups ?? []).filter((g) => g.sectorId === sectorId).map((g) => ({ value: g.groupId, label: g.name })),
  ];

  const groupLine = (entry: RosterEntry) => {
    if (entry.group) return `Groupe : ${entry.group.name}`;
    if (entry.demande?.status === "en_attente") return `Demande : ${entry.demande.group.name}`;
    return undefined;
  };

  const removeButton = (entry: RosterEntry) => (
    <Button type="button" label="Retirer" variant="outline" disabled={pending} onClick={() => removePerson.mutate(entry.personId)} />
  );

  /** Décision sur une Demande en attente, et sortie du Groupe pour qui y roule. */
  const groupActions = (entry: RosterEntry) => (
    <>
      {entry.demande?.status === "en_attente" && (
        <>
          <Button type="button" label="Accepter" disabled={pending} onClick={() => decide.mutate({ eventId, personId: entry.personId, accept: true })} />
          <Button
            type="button"
            label="Refuser"
            variant="outline"
            disabled={pending}
            onClick={() => decide.mutate({ eventId, personId: entry.personId, accept: false })}
          />
        </>
      )}
      {entry.group && (
        <Button type="button" label="Sortir du groupe" variant="outline" disabled={pending} onClick={() => takeOut.mutate({ eventId, personId: entry.personId })} />
      )}
    </>
  );

  const row = (entry: RosterEntry, actions: React.ReactNode) => (
    <li key={entry.personId} className={styles.row}>
      <span className={styles.person}>
        {entry.firstName} {entry.lastName}
        <span className={styles.phone}>{[entry.phone, groupLine(entry)].filter(Boolean).join(" · ")}</span>
      </span>
      {editable && <span className={styles.actions}>{actions}</span>}
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
              <>
                {!p.group && (
                  <Select
                    label="Groupe"
                    value=""
                    onChange={(groupId) => groupId && place.mutate({ personId: p.personId, groupId })}
                    options={groupOptions}
                  />
                )}
                {groupActions(p)}
                {removeButton(p)}
              </>
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
              <>
                <Button type="button" label="Faire monter" disabled={pending || full} onClick={() => moveUp.mutate(w.personId)} />
                {groupActions(w)}
                {removeButton(w)}
              </>
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
