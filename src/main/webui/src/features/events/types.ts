import { EventStatus } from "./status";

/** Reflète EventDto côté backend (vue Detailed). */
export interface EventParticipant {
  userId: string;
  firstName: string;
  lastName: string;
}

export interface Event {
  eventId: string;
  /** Statut courant (calculé par l'API à partir du statut enregistré et des dates). */
  status: EventStatus;
  name: string;
  description: string;
  startDateTime: string; // ISO 8601
  endDateTime: string; // ISO 8601
  /** Secteur auquel appartient l'Événement. */
  sectorId: string;
  imageUrl?: string;
  address?: string;
  addressComplement?: string;
  city?: string;
  postalCode?: string;
  country?: string;
  participants?: EventParticipant[];
}
