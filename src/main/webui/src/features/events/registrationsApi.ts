const STORAGE_KEY = "urue-my-event-registrations";

/**
 * Client mocké pour l'inscription aux événements (EVENT_PARTICIPANTS côté
 * backend). Persisté en localStorage en l'absence d'endpoint /api/events/{id}
 * de (dés)inscription ; même signature qu'un futur appel réel.
 */
function readIds(): string[] {
  try {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved ? JSON.parse(saved) : [];
  } catch {
    return [];
  }
}

function writeIds(ids: string[]) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(ids));
  } catch {
    // stockage indisponible : l'inscription reste active pour la session
  }
}

export async function fetchMyEventIds(): Promise<string[]> {
  return Promise.resolve(readIds());
}

export async function registerForEvent(eventId: string): Promise<void> {
  const ids = readIds();
  if (!ids.includes(eventId)) writeIds([...ids, eventId]);
  return Promise.resolve();
}

export async function unregisterFromEvent(eventId: string): Promise<void> {
  writeIds(readIds().filter((id) => id !== eventId));
  return Promise.resolve();
}
