import { mockFeatureFlags } from "./fixtures";
import { FeatureFlag, FeatureName } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";

const OVERRIDES_KEY = "urue-feature-flag-overrides";

/**
 * Client mocké — aucun endpoint /api/features n'existe encore côté backend
 * (service et entité présents, pas de contrôleur REST). Signature alignée
 * sur un futur GET/PATCH.
 */
export function createFeatureFlagsApi(store: JsonStore = localJsonStore) {
  const readOverrides = () => store.read<Partial<Record<FeatureName, boolean>>>(OVERRIDES_KEY, {});
  return {
    fetchFeatureFlags: async (): Promise<FeatureFlag[]> => {
      const overrides = readOverrides();
      return mockFeatureFlags.map((f) => ({ ...f, isActive: overrides[f.name] ?? f.isActive }));
    },
    setFeatureFlagActive: async (name: FeatureName, isActive: boolean): Promise<void> => {
      store.write(OVERRIDES_KEY, { ...readOverrides(), [name]: isActive });
    },
  };
}

export const { fetchFeatureFlags, setFeatureFlagActive } = createFeatureFlagsApi();
