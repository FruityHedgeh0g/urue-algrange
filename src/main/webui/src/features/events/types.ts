/** Reflète EventDto côté backend (vue Detailed). */
export interface EventOrganizer {
  userId: string;
  firstName: string;
  lastName: string;
}

export interface Event {
  eventId: string;
  status: string;
  name: string;
  description: string;
  startDateTime: string; // ISO 8601
  endDateTime: string; // ISO 8601
  creator: EventOrganizer;
  imageUrl?: string;
  address?: string;
  addressComplement?: string;
  city?: string;
  postalCode?: string;
  country?: string;
  participants?: EventOrganizer[];
  organizers?: EventOrganizer[];
}
