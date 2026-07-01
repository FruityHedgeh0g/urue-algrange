import { FeatureRequest } from "./types";

export const mockFeatureRequests: FeatureRequest[] = [
  {
    id: "fr-1",
    title: "Export CSV de la liste des inscrits",
    description: "Permettre au Bureau d'exporter la liste des inscrits à un événement pour l'organisation logistique.",
    createdAt: "2026-06-02T10:00:00",
    requestedBy: "Sophie Kremer",
  },
  {
    id: "fr-2",
    title: "Notifications par e-mail avant un événement",
    description: "Envoyer un rappel automatique aux inscrits 48h avant le départ.",
    createdAt: "2026-06-18T14:30:00",
    requestedBy: "Marc Weber",
  },
];
