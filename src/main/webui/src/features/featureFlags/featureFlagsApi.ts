import { mockFeatureFlags } from "./fixtures";
import { FeatureFlag } from "./types";

const OVERRIDES_KEY = "urue-feature-flag-overrides";

/**
 * Client mocké — aucun endpoint /api/features n'existe encore côté backend
 * (service et entité présents, pas de contrôleur REST). Signature alignée
 * sur un futur GET/PATCH.
 */
function readOverrides(): Record<string, boolean> {
  try {
    const saved = localStorage.getItem(OVERRIDES_KEY);
    return saved ? JSON.parse(saved) : {};
  } catch {
    return {};
  }
}

function writeOverrides(overrides: Record<string, boolean>) {
  try {
    localStorage.setItem(OVERRIDES_KEY, JSON.stringify(overrides));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

export async function fetchFeatureFlags(): Promise<FeatureFlag[]> {
  const overrides = readOverrides();
  return Promise.resolve(mockFeatureFlags.map((f) => ({ ...f, isActive: overrides[f.name] ?? f.isActive })));
}

export async function setFeatureFlagActive(name: string, isActive: boolean): Promise<void> {
  const overrides = readOverrides();
  overrides[name] = isActive;
  writeOverrides(overrides);
  return Promise.resolve();
}
