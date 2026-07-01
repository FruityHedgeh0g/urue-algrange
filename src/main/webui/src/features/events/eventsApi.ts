import { mockEvents } from "./fixtures";
import { Event } from "./types";

/** Client mocké — même signature qu'un futur GET /api/events (+ /api/events/{id}). */
export async function fetchEvents(): Promise<Event[]> {
  return Promise.resolve(mockEvents);
}

export async function fetchEventById(eventId: string): Promise<Event | undefined> {
  return Promise.resolve(mockEvents.find((e) => e.eventId === eventId));
}

export function isUpcoming(event: Event, now: Date = new Date()): boolean {
  return new Date(event.endDateTime) >= now;
}
