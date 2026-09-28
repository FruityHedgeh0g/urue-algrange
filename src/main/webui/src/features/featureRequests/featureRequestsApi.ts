import { mockFeatureRequests } from "./fixtures";
import { FeatureRequest } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";

const CREATED_KEY = "urue-feature-requests-created";

export function createFeatureRequestsApi(store: JsonStore = localJsonStore) {
  const readCreated = () => store.read<FeatureRequest[]>(CREATED_KEY, []);
  return {
    fetchFeatureRequests: async (): Promise<FeatureRequest[]> => [...readCreated(), ...mockFeatureRequests],
    createFeatureRequest: async (input: { title: string; description: string; requestedBy: string }): Promise<void> => {
      store.write(CREATED_KEY, [{ id: `fr-${Date.now()}`, createdAt: new Date().toISOString(), ...input }, ...readCreated()]);
    },
  };
}

export const { fetchFeatureRequests, createFeatureRequest } = createFeatureRequestsApi();
