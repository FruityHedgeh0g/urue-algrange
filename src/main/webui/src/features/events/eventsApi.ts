import { mockEvents } from "./fixtures";
import { Event, EventOrganizer } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké — le EventController backend n'expose que GET /api/events
 * pour l'instant (création/édition commentées). Mêmes signatures qu'un futur
 * POST/PATCH réel.
 */
export interface EventInput {
  name: string;
  description: string;
  startDateTime: string;
  endDateTime: string;
  imageUrl?: string;
  address?: string;
  city?: string;
  postalCode?: string;
  country?: string;
}

export function createEventsApi(store: JsonStore = localJsonStore) {
  const events = createOverlayCollection<Event>({ store, name: "event", fixtures: mockEvents, idOf: (e) => e.eventId });
  return {
    fetchEvents: () => events.list(),
    fetchEventById: (eventId: string) => events.get(eventId),
    updateEvent: (eventId: string, patch: EventInput) => events.update(eventId, patch),
    createEvent: (input: EventInput, creator: EventOrganizer) =>
      events.create({ eventId: `event-${Date.now()}`, status: "PUBLISHED", creator, ...input }),
    deleteEvent: (eventId: string) => events.remove(eventId),
  };
}

export const { fetchEvents, fetchEventById, updateEvent, createEvent, deleteEvent } = createEventsApi();

export function isUpcoming(event: Event, now: Date = new Date()): boolean {
  return new Date(event.endDateTime) >= now;
}
