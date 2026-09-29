import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createEventsApi } from "./eventsApi";
import { acceptsSignUps } from "./status";

const STORAGE_KEY = "urue-my-registrations";

/** Où en est une inscription : place confirmée (Participant) ou Liste d'attente. */
export type RegistrationStatus = "participant" | "en_attente";

/** Reflète RegistrationDto côté backend. */
export interface Registration {
  eventId: string;
  mode: "pilote";
  status: RegistrationStatus;
  signedUpAt: string;
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

/**
 * Client mocké, mêmes contrats que EventController : PUT/DELETE
 * /api/events/{eventId}/registration et GET /api/events/registrations.
 * Le mock ne connaît pas les inscriptions des autres : le maximum d'un
 * Événement n'y est pas appliqué (c'est le backend qui en décide).
 */
export function createRegistrationsApi(store: JsonStore = localJsonStore) {
  const events = createEventsApi(store);
  const read = () => store.read<Registration[]>(STORAGE_KEY, []);

  return {
    fetchMyRegistrations: async (): Promise<Registration[]> => read(),
    signUp: async (eventId: string, phone: string | undefined): Promise<Registration> => {
      const existing = read().find((r) => r.eventId === eventId);
      if (existing) return existing;

      const event = await events.fetchEventById(eventId, true);
      if (!event || !acceptsSignUps(event.status)) throw new Error("Les inscriptions sont fermées pour cet événement.");
      if (!phone?.trim()) throw new PhoneRequiredError();

      const registration: Registration = {
        eventId,
        mode: "pilote",
        status: event.status === "ouvert" ? "participant" : "en_attente",
        signedUpAt: new Date().toISOString(),
      };
      store.write(STORAGE_KEY, [...read(), registration]);
      return registration;
    },
    withdraw: async (eventId: string): Promise<void> => {
      if (!read().some((r) => r.eventId === eventId)) throw new Error("Aucune inscription à cet événement.");
      const event = await events.fetchEventById(eventId, true);
      if (event?.status === "archive") throw new Error("Un événement archivé garde ses participants.");
      store.write(STORAGE_KEY, read().filter((r) => r.eventId !== eventId));
    },
  };
}

export const { fetchMyRegistrations, signUp, withdraw } = createRegistrationsApi();
