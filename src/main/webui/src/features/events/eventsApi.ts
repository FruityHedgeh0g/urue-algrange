import { mockEvents } from "./fixtures";
import { Event, EventOrganizer } from "./types";

const OVERRIDES_KEY = "urue-event-overrides";
const CREATED_KEY = "urue-event-created";
const DELETED_KEY = "urue-event-deleted";

/**
 * Client mocké — le EventController backend n'expose que GET /api/events
 * pour l'instant (création/édition commentées). Mêmes signatures qu'un futur
 * POST/PATCH réel.
 */
function readJson<T>(key: string, fallback: T): T {
  try {
    const saved = localStorage.getItem(key);
    return saved ? JSON.parse(saved) : fallback;
  } catch {
    return fallback;
  }
}

function writeJson(key: string, value: unknown) {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

function readDeletedIds(): string[] {
  return readJson<string[]>(DELETED_KEY, []);
}

async function fetchAllRaw(): Promise<Event[]> {
  const overrides = readJson<Record<string, Partial<Event>>>(OVERRIDES_KEY, {});
  const created = readJson<Event[]>(CREATED_KEY, []);
  const deleted = readDeletedIds();
  const base = mockEvents.map((event) => ({ ...event, ...overrides[event.eventId] }));
  return [...base, ...created].filter((event) => !deleted.includes(event.eventId));
}

export async function fetchEvents(): Promise<Event[]> {
  return fetchAllRaw();
}

export async function fetchEventById(eventId: string): Promise<Event | undefined> {
  const all = await fetchAllRaw();
  return all.find((e) => e.eventId === eventId);
}

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

export async function updateEvent(eventId: string, patch: EventInput): Promise<void> {
  const overrides = readJson<Record<string, Partial<Event>>>(OVERRIDES_KEY, {});
  overrides[eventId] = { ...overrides[eventId], ...patch };
  writeJson(OVERRIDES_KEY, overrides);
  return Promise.resolve();
}

export async function createEvent(input: EventInput, creator: EventOrganizer): Promise<void> {
  const created = readJson<Event[]>(CREATED_KEY, []);
  created.push({ eventId: `event-${Date.now()}`, status: "PUBLISHED", creator, ...input });
  writeJson(CREATED_KEY, created);
  return Promise.resolve();
}

export async function deleteEvent(eventId: string): Promise<void> {
  const deleted = readDeletedIds();
  if (!deleted.includes(eventId)) writeJson(DELETED_KEY, [...deleted, eventId]);
  return Promise.resolve();
}

export function isUpcoming(event: Event, now: Date = new Date()): boolean {
  return new Date(event.endDateTime) >= now;
}
