import { Event } from "./types";

const creator = { userId: "user-1", firstName: "Marc", lastName: "Weber" };

export const mockEvents: Event[] = [
  {
    eventId: "event-1",
    status: "PUBLISHED",
    name: "Balade solidaire d'Algrange",
    description:
      "Grande balade moto au profit de la lutte contre le cancer, ouverte à tous les motards et leurs proches. Départ groupé, parcours de 80 km à travers le Pays-Haut, arrivée place de la mairie avec animations et restauration.",
    startDateTime: "2026-08-15T09:00:00",
    endDateTime: "2026-08-15T18:00:00",
    creator,
    address: "Place de la Mairie",
    city: "Algrange",
    postalCode: "57440",
    country: "France",
  },
  {
    eventId: "event-2",
    status: "PUBLISHED",
    name: "Soirée caritative - Loto solidaire",
    description:
      "Une soirée conviviale au profit de l'association : loto, buvette et petite restauration. Tous les bénéfices sont reversés à la recherche contre le cancer.",
    startDateTime: "2026-10-04T19:00:00",
    endDateTime: "2026-10-04T23:00:00",
    creator,
    address: "Salle des fêtes",
    city: "Algrange",
    postalCode: "57440",
    country: "France",
  },
  {
    eventId: "event-3",
    status: "PUBLISHED",
    name: "Collecte 2026 - Édition passée",
    description:
      "Merci à tous les participants de cette édition ! Retrouvez le récapitulatif dans nos actualités.",
    startDateTime: "2026-05-10T09:00:00",
    endDateTime: "2026-05-10T18:00:00",
    creator,
    address: "Place de la Mairie",
    city: "Algrange",
    postalCode: "57440",
    country: "France",
  },
];
