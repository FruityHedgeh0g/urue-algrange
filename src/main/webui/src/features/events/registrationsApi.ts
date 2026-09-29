import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createEventsApi } from "./eventsApi";
import { acceptsSignUps } from "./status";

const ROSTERS_KEY = "urue-event-rosters";

/** Où en est une inscription : place confirmée (Participant) ou Liste d'attente. */
export type RegistrationStatus = "participant" | "en_attente";

/** Reflète RegistrationDto côté backend : sa propre inscription à un Événement. */
export interface Registration {
  eventId: string;
  mode: "pilote";
  status: RegistrationStatus;
  signedUpAt: string;
}

/** Reflète RosterEntryDto côté backend : une personne inscrite, vue par le Bureau. */
export interface RosterEntry {
  personId: string;
  firstName: string;
  lastName: string;
  phone: string;
  mode: "pilote";
  signedUpAt: string;
}

/** Reflète RosterDto : Participants et Liste d'attente, chacun dans l'ordre d'inscription. */
export interface Roster {
  eventId: string;
  maxParticipants: number | null;
  participants: RosterEntry[];
  waiting: RosterEntry[];
}

/** Personne qui s'inscrit (le backend lit ces informations dans son profil). */
export interface SigningUpPerson {
  userId: string;
  firstName: string;
  lastName: string;
  phone?: string;
}

export const REGISTRATION_STATUS_LABELS: Record<RegistrationStatus, string> = {
  participant: "Participant",
  en_attente: "En attente",
};

/** Réponse 422 `phone-required` de l'API : il faut un téléphone pour s'inscrire. */
export class PhoneRequiredError extends Error {
  constructor() {
    super("Un numéro de téléphone est nécessaire pour s'inscrire.");
    this.name = "PhoneRequiredError";
  }
}

type StoredEntry = RosterEntry & { waiting: boolean };

/**
 * Client mocké, mêmes contrats que EventController : PUT/DELETE
 * /api/events/{eventId}/registration, GET /api/events/registrations, et pour
 * le Bureau GET /api/events/{eventId}/roster, POST .../roster/{personId}/promote,
 * DELETE .../roster/{personId}. Une seule liste par Événement alimente les deux vues.
 */
export function createRegistrationsApi(store: JsonStore = localJsonStore) {
  const events = createEventsApi(store);
  const readAll = () => store.read<Record<string, StoredEntry[]>>(ROSTERS_KEY, {});
  const read = (eventId: string) => readAll()[eventId] ?? [];
  const write = (eventId: string, entries: StoredEntry[]) => store.write(ROSTERS_KEY, { ...readAll(), [eventId]: entries });

  const toRegistration = (eventId: string, entry: StoredEntry): Registration => ({
    eventId,
    mode: entry.mode,
    status: entry.waiting ? "en_attente" : "participant",
    signedUpAt: entry.signedUpAt,
  });

  const eventOrThrow = async (eventId: string) => {
    const event = await events.fetchEventById(eventId, true);
    if (!event) throw new Error("Événement introuvable.");
    return event;
  };

  const placeLeft = (eventId: string, max: number | null | undefined) =>
    !max || read(eventId).filter((e) => !e.waiting).length < max;

  const fetchRoster = async (eventId: string): Promise<Roster> => {
    const event = await eventOrThrow(eventId);
    const entries = [...read(eventId)].sort((a, b) => a.signedUpAt.localeCompare(b.signedUpAt));
    const strip = ({ waiting: _waiting, ...entry }: StoredEntry): RosterEntry => entry;
    return {
      eventId,
      maxParticipants: event.maxParticipants ?? null,
      participants: entries.filter((e) => !e.waiting).map(strip),
      waiting: entries.filter((e) => e.waiting).map(strip),
    };
  };

  /** Un Événement archivé garde ses inscrits. */
  const refuseOnArchived = async (eventId: string) => {
    if ((await eventOrThrow(eventId)).status === "archive") throw new Error("Un événement archivé garde ses inscrits.");
  };

  return {
    fetchMyRegistrations: async (personId: string): Promise<Registration[]> =>
      Object.entries(readAll()).flatMap(([eventId, entries]) =>
        entries.filter((e) => e.personId === personId).map((e) => toRegistration(eventId, e))
      ),

    signUp: async (eventId: string, person: SigningUpPerson): Promise<Registration> => {
      const existing = read(eventId).find((e) => e.personId === person.userId);
      if (existing) return toRegistration(eventId, existing);

      const event = await eventOrThrow(eventId);
      if (!acceptsSignUps(event.status)) throw new Error("Les inscriptions sont fermées pour cet événement.");
      if (!person.phone?.trim()) throw new PhoneRequiredError();

      const entry: StoredEntry = {
        personId: person.userId,
        firstName: person.firstName,
        lastName: person.lastName,
        phone: person.phone.trim(),
        mode: "pilote",
        // Horodatage strictement croissant : l'ordre d'inscription départage la liste d'attente
        signedUpAt: new Date(Math.max(Date.now(), ...read(eventId).map((e) => Date.parse(e.signedUpAt) + 1))).toISOString(),
        waiting: !(event.status === "ouvert" && placeLeft(eventId, event.maxParticipants)),
      };
      write(eventId, [...read(eventId), entry]);
      return toRegistration(eventId, entry);
    },

    withdraw: async (eventId: string, personId: string): Promise<void> => {
      if (!read(eventId).some((e) => e.personId === personId)) throw new Error("Aucune inscription à cet événement.");
      await refuseOnArchived(eventId);
      write(eventId, read(eventId).filter((e) => e.personId !== personId));
    },

    fetchRoster,

    promote: async (eventId: string, personId: string): Promise<Roster> => {
      const event = await eventOrThrow(eventId);
      const entry = read(eventId).find((e) => e.personId === personId);
      if (!entry?.waiting) throw new Error("Cette personne n'est pas sur la liste d'attente.");
      await refuseOnArchived(eventId);
      if (!placeLeft(eventId, event.maxParticipants)) throw new Error("L'événement a atteint son maximum de participants.");
      write(eventId, read(eventId).map((e) => (e.personId === personId ? { ...e, waiting: false } : e)));
      return fetchRoster(eventId);
    },

    remove: async (eventId: string, personId: string): Promise<Roster> => {
      if (!read(eventId).some((e) => e.personId === personId)) throw new Error("Aucune inscription à cet événement.");
      await refuseOnArchived(eventId);
      write(eventId, read(eventId).filter((e) => e.personId !== personId));
      return fetchRoster(eventId);
    },
  };
}

export const { fetchMyRegistrations, signUp, withdraw, fetchRoster, promote, remove } = createRegistrationsApi();
