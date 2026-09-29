import { mockEvents } from "./fixtures";
import { Event } from "./types";
import { allowedTransitions, currentStatus, EventStatus } from "./status";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké, mêmes contrats que EventController : GET (sans Planification
 * sous le Bureau), POST (démarre en Planification, Secteur requis), PATCH,
 * PUT /api/events/{eventId}/status. Pas de suppression : on annule.
 */
export interface EventInput {
  name: string;
  description: string;
  startDateTime: string;
  endDateTime: string;
  sectorId: string;
  /** 0 retire le maximum, comme côté backend. */
  maxParticipants?: number;
  imageUrl?: string;
  address?: string;
  city?: string;
  postalCode?: string;
  country?: string;
}

export function createEventsApi(store: JsonStore = localJsonStore) {
  const events = createOverlayCollection<Event>({ store, name: "event", fixtures: mockEvents, idOf: (e) => e.eventId });
  /** Le statut renvoyé est le statut courant, comme côté backend. */
  const withCurrentStatus = (event: Event): Event => ({ ...event, status: currentStatus(event) });

  return {
    fetchEvents: async (seesPlanification: boolean) =>
      (await events.list()).filter((e) => seesPlanification || e.status !== "planification").map(withCurrentStatus),
    fetchEventById: async (eventId: string, seesPlanification: boolean) => {
      const event = await events.get(eventId);
      return event && (seesPlanification || event.status !== "planification") ? withCurrentStatus(event) : undefined;
    },
    updateEvent: (eventId: string, patch: Omit<EventInput, "sectorId">) =>
      events.update(eventId, { ...patch, maxParticipants: patch.maxParticipants ? patch.maxParticipants : null }),
    createEvent: async (input: EventInput): Promise<Event> => {
      const event: Event = { eventId: `event-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`, status: "planification", ...input };
      await events.create(event);
      return event;
    },
    changeStatus: async (eventId: string, status: EventStatus) => {
      const event = await events.get(eventId);
      if (!event || !allowedTransitions(currentStatus(event)).includes(status)) {
        throw new Error("Transition de statut refusée");
      }
      await events.update(eventId, { status });
      return withCurrentStatus({ ...event, status });
    },
  };
}

export const { fetchEvents, fetchEventById, updateEvent, createEvent, changeStatus } = createEventsApi();

export function isUpcoming(event: Event, now: Date = new Date()): boolean {
  return new Date(event.endDateTime) >= now;
}
