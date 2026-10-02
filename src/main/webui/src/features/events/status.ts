/**
 * Statut d'un Événement (miroir de EventStatusEnum côté backend) :
 * Planification → Ouvert ⇄ Complet à la main, puis En cours et Archivé selon
 * les dates ; Annulé à la main tant que l'Événement n'est pas archivé.
 */
export type EventStatus = "planification" | "ouvert" | "complet" | "en_cours" | "archive" | "annule";

export const EVENT_STATUS_LABELS: Record<EventStatus, string> = {
  planification: "Planification",
  ouvert: "Ouvert",
  complet: "Complet",
  en_cours: "En cours",
  archive: "Archivé",
  annule: "Annulé",
};

/** Statut courant d'un Événement enregistré avec `status` : seuls Ouvert et Complet suivent les dates. */
export function currentStatus(
  event: { status: EventStatus; startDateTime: string; endDateTime: string },
  now: Date = new Date()
): EventStatus {
  if (event.status !== "ouvert" && event.status !== "complet") return event.status;
  if (now > new Date(event.endDateTime)) return "archive";
  if (now >= new Date(event.startDateTime)) return "en_cours";
  return event.status;
}

/** Les inscriptions sont possibles tant que l'Événement est Ouvert ou Complet (liste d'attente). */
export function acceptsSignUps(status: EventStatus): boolean {
  return status === "ouvert" || status === "complet";
}

/** Ton du badge de statut : mis en avant tant qu'on peut s'inscrire. */
export function statusTone(status: EventStatus): "accent" | "primary" | "muted" {
  if (acceptsSignUps(status)) return "accent";
  return status === "en_cours" ? "primary" : "muted";
}

/** Transitions manuelles proposées au Bureau depuis le statut courant. */
export function allowedTransitions(current: EventStatus): EventStatus[] {
  switch (current) {
    case "planification":
      return ["ouvert", "annule"];
    case "ouvert":
      return ["complet", "annule"];
    case "complet":
      return ["ouvert", "annule"];
    case "en_cours":
      return ["annule"];
    default:
      return [];
  }
}
