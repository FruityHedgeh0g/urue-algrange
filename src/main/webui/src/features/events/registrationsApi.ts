import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";

const STORAGE_KEY = "urue-my-event-registrations";

/**
 * Client mocké pour l'inscription aux événements (EVENT_PARTICIPANTS côté
 * backend). Persisté en localStorage en l'absence d'endpoint /api/events/{id}
 * de (dés)inscription ; même signature qu'un futur appel réel.
 */
export function createRegistrationsApi(store: JsonStore = localJsonStore) {
  const readIds = () => store.read<string[]>(STORAGE_KEY, []);
  return {
    fetchMyEventIds: async (): Promise<string[]> => readIds(),
    registerForEvent: async (eventId: string): Promise<void> => {
      const ids = readIds();
      if (!ids.includes(eventId)) store.write(STORAGE_KEY, [...ids, eventId]);
    },
    unregisterFromEvent: async (eventId: string): Promise<void> => {
      store.write(STORAGE_KEY, readIds().filter((id) => id !== eventId));
    },
  };
}

export const { fetchMyEventIds, registerForEvent, unregisterFromEvent } = createRegistrationsApi();
