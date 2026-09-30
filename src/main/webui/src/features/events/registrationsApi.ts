import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createEventsApi } from "./eventsApi";
import { createGroupsApi } from "../groups/groupsApi";
import { Group } from "../groups/types";
import { acceptsSignUps, EventStatus } from "./status";

const ROSTERS_KEY = "urue-event-rosters";
const MAXIMUMS_KEY = "urue-event-group-maximums";

/** Où en est une inscription : place confirmée (Participant) ou Liste d'attente. */
export type RegistrationStatus = "participant" | "en_attente";

/** Où en est une Demande de groupe. */
export type DemandeStatus = "en_attente" | "acceptee" | "refusee";

/** Reflète GroupRefDto côté backend. */
export interface GroupRef {
  groupId: string;
  name: string;
}

/** Reflète DemandeDto : le Groupe demandé et l'état de la Demande. */
export interface Demande {
  group: GroupRef;
  status: DemandeStatus;
}

/** Reflète RegistrationDto côté backend : sa propre inscription à un Événement. */
export interface Registration {
  eventId: string;
  mode: "pilote";
  status: RegistrationStatus;
  signedUpAt: string;
  /** Groupe avec lequel on roule à cet Événement, une fois accepté ou placé. */
  group: GroupRef | null;
  demande: Demande | null;
}

/** Reflète RosterEntryDto côté backend : une personne inscrite, vue par le Bureau ou son Chef de groupe. */
export interface RosterEntry {
  personId: string;
  firstName: string;
  lastName: string;
  phone: string;
  mode: "pilote";
  signedUpAt: string;
  group: GroupRef | null;
  demande: Demande | null;
}

/**
 * Reflète GroupRosterDto : un Groupe à un Événement, son maximum (null : sans
 * limite), qui y roule, et ses Demandes en attente dans l'ordre d'inscription —
 * la Liste d'attente du Groupe une fois le maximum atteint.
 */
export interface GroupRoster {
  group: GroupRef;
  maximum: number | null;
  members: RosterEntry[];
  demandes: RosterEntry[];
}

/** Reflète RosterDto : Participants et Liste d'attente, chacun dans l'ordre d'inscription, et chaque Groupe du Secteur. */
export interface Roster {
  eventId: string;
  maxParticipants: number | null;
  participants: RosterEntry[];
  waiting: RosterEntry[];
  groups: GroupRoster[];
}

/** Reflète MonGroupeDto : le Groupe mené (null sans Affectation) et, par Événement, ses membres et Demandes en attente. */
export interface MonGroupe {
  group: GroupRef | null;
  events: {
    eventId: string;
    name: string;
    startDateTime: string;
    status: EventStatus;
    /** Maximum du Groupe à cet Événement, null : sans limite. */
    maximum: number | null;
    members: RosterEntry[];
    demandes: RosterEntry[];
  }[];
}

/** Qui agit : un Chef n'agit que sur le Groupe qu'il mène (Affectation), le Bureau sur tous. */
export interface Actor {
  personId: string;
  bureau: boolean;
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

export const DEMANDE_STATUS_LABELS: Record<DemandeStatus, string> = {
  en_attente: "en attente",
  acceptee: "acceptée",
  refusee: "refusée",
};

/** Réponse 422 `phone-required` de l'API : il faut un téléphone pour s'inscrire. */
export class PhoneRequiredError extends Error {
  constructor() {
    super("Un numéro de téléphone est nécessaire pour s'inscrire.");
    this.name = "PhoneRequiredError";
  }
}

interface StoredEntry {
  personId: string;
  firstName: string;
  lastName: string;
  phone: string;
  mode: "pilote";
  signedUpAt: string;
  waiting: boolean;
  groupId?: string | null;
  demandeGroupId?: string | null;
  demandeStatus?: DemandeStatus | null;
}

/**
 * Client mocké, mêmes contrats que EventController : inscription (PUT/DELETE
 * /api/events/{eventId}/registration, ?groupId= pour une Demande de groupe),
 * nouvelle Demande (PUT .../registration/demande), décision (POST
 * .../demandes/{personId}/accept|refuse), liste du Bureau (GET .../roster,
 * POST .../roster/{personId}/promote, DELETE .../roster/{personId}, PUT et
 * DELETE .../roster/{personId}/group), maximum d'un Groupe (PUT
 * .../groups/{groupId}/maximum) et Mon groupe (GET /api/events/mon-groupe).
 * Une seule liste par Événement alimente toutes ces vues.
 */
export function createRegistrationsApi(store: JsonStore = localJsonStore) {
  const events = createEventsApi(store);
  const groups = createGroupsApi(store);
  const readAll = () => store.read<Record<string, StoredEntry[]>>(ROSTERS_KEY, {});
  const read = (eventId: string) => readAll()[eventId] ?? [];
  const write = (eventId: string, entries: StoredEntry[]) => store.write(ROSTERS_KEY, { ...readAll(), [eventId]: entries });
  const update = (eventId: string, personId: string, patch: Partial<StoredEntry>) =>
    write(eventId, read(eventId).map((e) => (e.personId === personId ? { ...e, ...patch } : e)));
  const readAllMaximums = () => store.read<Record<string, Record<string, number>>>(MAXIMUMS_KEY, {});
  const maximumsOf = (eventId: string) => readAllMaximums()[eventId] ?? {};

  const ref = (all: Group[], groupId: string | null | undefined): GroupRef | null => {
    const group = all.find((g) => g.groupId === groupId);
    return group ? { groupId: group.groupId, name: group.name } : null;
  };

  const toEntry = (all: Group[], { waiting: _waiting, groupId, demandeGroupId, demandeStatus, ...entry }: StoredEntry): RosterEntry => {
    const demandeGroup = ref(all, demandeGroupId);
    return { ...entry, group: ref(all, groupId), demande: demandeStatus && demandeGroup ? { group: demandeGroup, status: demandeStatus } : null };
  };

  const toRegistration = async (eventId: string, entry: StoredEntry): Promise<Registration> => {
    const { group, demande } = toEntry(await groups.fetchGroups(), entry);
    return { eventId, mode: entry.mode, status: entry.waiting ? "en_attente" : "participant", signedUpAt: entry.signedUpAt, group, demande };
  };

  const eventOrThrow = async (eventId: string) => {
    const event = await events.fetchEventById(eventId, true);
    if (!event) throw new Error("Événement introuvable.");
    return event;
  };

  const entryOrThrow = (eventId: string, personId: string) => {
    const entry = read(eventId).find((e) => e.personId === personId);
    if (!entry) throw new Error("Aucune inscription à cet événement.");
    return entry;
  };

  const groupOrThrow = async (groupId: string) => {
    const group = (await groups.fetchGroups()).find((g) => g.groupId === groupId);
    if (!group) throw new Error("Groupe introuvable.");
    return group;
  };

  /** Un Chef n'agit que sur le Groupe qu'il mène ; le Bureau sur tous. */
  const requireLeaderOrBureau = async (actor: Actor, groupId: string | null | undefined) => {
    const group = (await groups.fetchGroups()).find((g) => g.groupId === groupId);
    if (!actor.bureau && group?.chef?.userId !== actor.personId) throw new Error("Vous ne menez pas ce groupe.");
  };

  const placeLeft = (eventId: string, max: number | null | undefined) =>
    !max || read(eventId).filter((e) => !e.waiting).length < max;

  const bySignUp = (a: StoredEntry, b: StoredEntry) => a.signedUpAt.localeCompare(b.signedUpAt);

  const ridesWith = (groupId: string) => (e: StoredEntry) => e.groupId === groupId;
  /** Demande en attente pour ce Groupe : sa Liste d'attente une fois le maximum atteint. */
  const asksFor = (groupId: string) => (e: StoredEntry) => e.demandeGroupId === groupId && e.demandeStatus === "en_attente";

  /** Au maximum du Groupe, personne de plus n'y entre ; sans maximum, pas de limite. */
  const requireRoomIn = (eventId: string, groupId: string) => {
    const maximum = maximumsOf(eventId)[groupId];
    if (maximum && read(eventId).filter(ridesWith(groupId)).length >= maximum)
      throw new Error(`Le groupe a atteint son maximum de ${maximum} pour cet événement.`);
  };

  const fetchRoster = async (eventId: string): Promise<Roster> => {
    const event = await eventOrThrow(eventId);
    const all = await groups.fetchGroups();
    const entries = [...read(eventId)].sort(bySignUp);
    const maximums = maximumsOf(eventId);
    return {
      eventId,
      maxParticipants: event.maxParticipants ?? null,
      participants: entries.filter((e) => !e.waiting).map((e) => toEntry(all, e)),
      waiting: entries.filter((e) => e.waiting).map((e) => toEntry(all, e)),
      groups: all
        .filter((g) => g.sectorId === event.sectorId)
        .sort((a, b) => a.name.localeCompare(b.name))
        .map((g) => ({
          group: { groupId: g.groupId, name: g.name },
          maximum: maximums[g.groupId] ?? null,
          members: entries.filter(ridesWith(g.groupId)).map((e) => toEntry(all, e)),
          demandes: entries.filter(asksFor(g.groupId)).map((e) => toEntry(all, e)),
        })),
    };
  };

  /** Un Événement archivé garde ses inscrits. */
  const refuseOnArchived = async (eventId: string) => {
    if ((await eventOrThrow(eventId)).status === "archive") throw new Error("Un événement archivé garde ses inscrits.");
  };

  const requestGroup = async (eventId: string, personId: string, groupId: string): Promise<Registration> => {
    await refuseOnArchived(eventId);
    const entry = entryOrThrow(eventId, personId);
    if (entry.groupId) throw new Error("Vous roulez déjà avec un groupe à cet événement.");
    await groupOrThrow(groupId);
    update(eventId, personId, { demandeGroupId: groupId, demandeStatus: "en_attente" });
    return toRegistration(eventId, entryOrThrow(eventId, personId));
  };

  return {
    fetchMyRegistrations: async (personId: string): Promise<Registration[]> =>
      Promise.all(
        Object.entries(readAll()).flatMap(([eventId, entries]) =>
          entries.filter((e) => e.personId === personId).map((e) => toRegistration(eventId, e))
        )
      ),

    signUp: async (eventId: string, person: SigningUpPerson, groupId?: string): Promise<Registration> => {
      const existing = read(eventId).find((e) => e.personId === person.userId);
      if (existing) return toRegistration(eventId, existing);

      const event = await eventOrThrow(eventId);
      if (!acceptsSignUps(event.status)) throw new Error("Les inscriptions sont fermées pour cet événement.");
      if (!person.phone?.trim()) throw new PhoneRequiredError();
      if (groupId) await groupOrThrow(groupId);

      const entry: StoredEntry = {
        personId: person.userId,
        firstName: person.firstName,
        lastName: person.lastName,
        phone: person.phone.trim(),
        mode: "pilote",
        // Horodatage strictement croissant : l'ordre d'inscription départage la liste d'attente
        signedUpAt: new Date(Math.max(Date.now(), ...read(eventId).map((e) => Date.parse(e.signedUpAt) + 1))).toISOString(),
        waiting: !(event.status === "ouvert" && placeLeft(eventId, event.maxParticipants)),
        demandeGroupId: groupId ?? null,
        demandeStatus: groupId ? "en_attente" : null,
      };
      write(eventId, [...read(eventId), entry]);
      return toRegistration(eventId, entry);
    },

    withdraw: async (eventId: string, personId: string): Promise<void> => {
      entryOrThrow(eventId, personId);
      await refuseOnArchived(eventId);
      write(eventId, read(eventId).filter((e) => e.personId !== personId));
    },

    requestGroup,

    decideDemande: async (eventId: string, personId: string, actor: Actor, accept: boolean): Promise<Registration> => {
      await refuseOnArchived(eventId);
      const entry = entryOrThrow(eventId, personId);
      if (entry.demandeStatus !== "en_attente") throw new Error("Aucune Demande de groupe en attente.");
      await requireLeaderOrBureau(actor, entry.demandeGroupId);
      // Au-delà du maximum, la Demande reste en attente, sur la Liste d'attente du Groupe
      if (accept && entry.demandeGroupId) requireRoomIn(eventId, entry.demandeGroupId);
      update(eventId, personId, accept ? { groupId: entry.demandeGroupId, demandeStatus: "acceptee" } : { demandeStatus: "refusee" });
      return toRegistration(eventId, entryOrThrow(eventId, personId));
    },

    placeInGroup: async (eventId: string, personId: string, groupId: string): Promise<Roster> => {
      await refuseOnArchived(eventId);
      const entry = entryOrThrow(eventId, personId);
      if (entry.waiting) throw new Error("Seul un Participant est placé dans un groupe.");
      await groupOrThrow(groupId);
      if (entry.groupId !== groupId) requireRoomIn(eventId, groupId);
      update(eventId, personId, { groupId, demandeGroupId: groupId, demandeStatus: "acceptee" });
      return fetchRoster(eventId);
    },

    /** Le maximum d'un Groupe du Secteur de l'Événement ; null ou 0 le retire. */
    setGroupMaximum: async (eventId: string, groupId: string, maximum: number | null): Promise<Roster> => {
      const event = await eventOrThrow(eventId);
      if (event.status === "archive" || event.status === "annule") throw new Error("Un événement archivé ou annulé n'est plus modifié.");
      if ((await groupOrThrow(groupId)).sectorId !== event.sectorId) throw new Error("Ce groupe n'est pas du secteur de l'événement.");
      const { [groupId]: _previous, ...others } = maximumsOf(eventId);
      store.write(MAXIMUMS_KEY, { ...readAllMaximums(), [eventId]: maximum && maximum > 0 ? { ...others, [groupId]: maximum } : others });
      return fetchRoster(eventId);
    },

    takeOutOfGroup: async (eventId: string, personId: string, actor: Actor): Promise<Registration> => {
      await refuseOnArchived(eventId);
      const entry = entryOrThrow(eventId, personId);
      if (!entry.groupId) throw new Error("Cette personne ne roule avec aucun groupe.");
      await requireLeaderOrBureau(actor, entry.groupId);
      update(eventId, personId, { groupId: null, demandeGroupId: null, demandeStatus: null });
      return toRegistration(eventId, entryOrThrow(eventId, personId));
    },

    fetchMonGroupe: async (chefId: string): Promise<MonGroupe> => {
      const all = await groups.fetchGroups();
      const led = all.find((g) => g.chef?.userId === chefId);
      if (!led) return { group: null, events: [] };

      const concerns = (e: StoredEntry) => ridesWith(led.groupId)(e) || asksFor(led.groupId)(e);
      // Tous les Événements du Secteur que le Chef prépare, même sans personne encore
      const eventsOfSecteur = (await events.fetchEvents(true)).filter(
        (e) => e.sectorId === led.sectorId && (acceptsSignUps(e.status) || e.status === "en_cours")
      );
      const withEntries = await Promise.all(
        Object.entries(readAll())
          .filter(([, entries]) => entries.some(concerns))
          .map(async ([eventId]) => events.fetchEventById(eventId, true))
      );
      const concerned = [...eventsOfSecteur, ...withEntries.filter((e) => e && !eventsOfSecteur.some((s) => s.eventId === e.eventId))];
      const rosters = concerned.map((event) => ({ event, entries: event ? read(event.eventId) : [] }));
      return {
        group: { groupId: led.groupId, name: led.name },
        events: rosters
          .filter(({ event }) => event && event.status !== "archive")
          .map(({ event, entries }) => ({
            eventId: event!.eventId,
            name: event!.name,
            startDateTime: event!.startDateTime,
            status: event!.status,
            maximum: maximumsOf(event!.eventId)[led.groupId] ?? null,
            members: [...entries].sort(bySignUp).filter(ridesWith(led.groupId)).map((e) => toEntry(all, e)),
            demandes: [...entries].sort(bySignUp).filter(asksFor(led.groupId)).map((e) => toEntry(all, e)),
          }))
          .sort((a, b) => a.startDateTime.localeCompare(b.startDateTime)),
      };
    },

    fetchRoster,

    promote: async (eventId: string, personId: string): Promise<Roster> => {
      const event = await eventOrThrow(eventId);
      const entry = read(eventId).find((e) => e.personId === personId);
      if (!entry?.waiting) throw new Error("Cette personne n'est pas sur la liste d'attente.");
      await refuseOnArchived(eventId);
      if (!placeLeft(eventId, event.maxParticipants)) throw new Error("L'événement a atteint son maximum de participants.");
      update(eventId, personId, { waiting: false });
      return fetchRoster(eventId);
    },

    remove: async (eventId: string, personId: string): Promise<Roster> => {
      entryOrThrow(eventId, personId);
      await refuseOnArchived(eventId);
      write(eventId, read(eventId).filter((e) => e.personId !== personId));
      return fetchRoster(eventId);
    },
  };
}

export const {
  fetchMyRegistrations,
  signUp,
  withdraw,
  requestGroup,
  decideDemande,
  placeInGroup,
  setGroupMaximum,
  takeOutOfGroup,
  fetchMonGroupe,
  fetchRoster,
  promote,
  remove,
} = createRegistrationsApi();
