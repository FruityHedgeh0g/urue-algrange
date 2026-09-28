import { mockConfigurations } from "./fixtures";
import { Configuration } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";

const OVERRIDES_KEY = "urue-configuration-overrides";

/**
 * Client mocké — le ConfigurationController backend n'expose que GET
 * /api/configurations (l'accès par nom et l'édition sont commentés / absents).
 */
export function createConfigurationsApi(store: JsonStore = localJsonStore) {
  const readOverrides = () => store.read<Record<string, string>>(OVERRIDES_KEY, {});
  return {
    fetchConfigurations: async (): Promise<Configuration[]> => {
      const overrides = readOverrides();
      return mockConfigurations.map((c) => ({ ...c, value: overrides[c.name] ?? c.value }));
    },
    updateConfiguration: async (name: string, value: string): Promise<void> => {
      store.write(OVERRIDES_KEY, { ...readOverrides(), [name]: value });
    },
  };
}

export const { fetchConfigurations, updateConfiguration } = createConfigurationsApi();
