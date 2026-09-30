import { mockEvents } from "./fixtures";
import { Event } from "./types";
import { allowedTransitions, currentStatus, EventStatus } from "./status";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";
import { closedSectorIds, refuseInClosedSector } from "../sectors/closedSectors";

/**
 * Client mocké, mêmes contrats que EventController : GET (sans Planification
 * sous le Bureau), POST (démarre en Planification, Secteur requis), PATCH,
 * PUT /api/events/{eventId}/status. Pas de suppression : on annule. Les
 * Événements d'un Secteur fermé ne sont vus que du Super admin
 * (`seesClosedSecteurs`) et ne changent plus.
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

  const visible = async (seesPlanification: boolean, seesClosedSecteurs: boolean) => {
    const closed = seesClosedSecteurs ? new Set<string>() : await closedSectorIds(store);
    return (event: Event) => (seesPlanification || event.status !== "planification") && !closed.has(event.sectorId);
  };
  const refuseWhenClosed = async (eventId: string) => refuseInClosedSector(store, (await events.get(eventId))?.sectorId);

  return {
    fetchEvents: async (seesPlanification: boolean, seesClosedSecteurs = false) => {
      const isVisible = await visible(seesPlanification, seesClosedSecteurs);
      return (await events.list()).filter(isVisible).map(withCurrentStatus);
    },
    fetchEventById: async (eventId: string, seesPlanification: boolean, seesClosedSecteurs = false) => {
      const event = await events.get(eventId);
      return event && (await visible(seesPlanification, seesClosedSecteurs))(event) ? withCurrentStatus(event) : undefined;
    },
    updateEvent: async (eventId: string, patch: Omit<EventInput, "sectorId">) => {
      await refuseWhenClosed(eventId);
      await events.update(eventId, { ...patch, maxParticipants: patch.maxParticipants ? patch.maxParticipants : null });
    },
    /** À la fermeture d'un Secteur : Archivé s'il a eu (ou a) lieu, Annulé sinon ; les inscriptions restent. */
    closeEventsOfSector: async (sectorId: string) => {
      for (const event of (await events.list()).filter((e) => e.sectorId === sectorId)) {
        const current = currentStatus(event);
        await events.update(event.eventId, { status: current === "en_cours" || current === "archive" ? "archive" : "annule" });
      }
    },
    createEvent: async (input: EventInput): Promise<Event> => {
      await refuseInClosedSector(store, input.sectorId);
      const event: Event = { eventId: `event-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`, status: "planification", ...input };
      await events.create(event);
      return event;
    },
    changeStatus: async (eventId: string, status: EventStatus) => {
      await refuseWhenClosed(eventId);
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
