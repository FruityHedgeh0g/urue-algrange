import { mockConfigurations } from "./fixtures";
import { Configuration } from "./types";

const OVERRIDES_KEY = "urue-configuration-overrides";

/**
 * Client mocké — le ConfigurationController backend n'expose que GET
 * /api/configurations (l'accès par nom et l'édition sont commentés / absents).
 */
function readOverrides(): Record<string, string> {
  try {
    const saved = localStorage.getItem(OVERRIDES_KEY);
    return saved ? JSON.parse(saved) : {};
  } catch {
    return {};
  }
}

function writeOverrides(overrides: Record<string, string>) {
  try {
    localStorage.setItem(OVERRIDES_KEY, JSON.stringify(overrides));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

export async function fetchConfigurations(): Promise<Configuration[]> {
  const overrides = readOverrides();
  return Promise.resolve(mockConfigurations.map((c) => ({ ...c, value: overrides[c.name] ?? c.value })));
}

export async function updateConfiguration(name: string, value: string): Promise<void> {
  const overrides = readOverrides();
  overrides[name] = value;
  writeOverrides(overrides);
  return Promise.resolve();
}
