import { mockSectors } from "./fixtures";
import { Sector } from "./types";

const OVERRIDES_KEY = "urue-sector-overrides";

/**
 * Client mocké — le backend a un SectorController mais les endpoints de
 * lecture par id et d'édition sont commentés côté Java. Les modifications
 * sont donc persistées en localStorage en attendant, avec les mêmes
 * signatures qu'un futur GET/PATCH /api/sectors/{id}.
 */
function readOverrides(): Record<string, Partial<Sector>> {
  try {
    const saved = localStorage.getItem(OVERRIDES_KEY);
    return saved ? JSON.parse(saved) : {};
  } catch {
    return {};
  }
}

function writeOverrides(overrides: Record<string, Partial<Sector>>) {
  try {
    localStorage.setItem(OVERRIDES_KEY, JSON.stringify(overrides));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

export async function fetchSectorById(sectorId: string): Promise<Sector | undefined> {
  const base = mockSectors.find((s) => s.sectorId === sectorId);
  if (!base) return Promise.resolve(undefined);
  return Promise.resolve({ ...base, ...readOverrides()[sectorId] });
}

export async function updateSector(sectorId: string, patch: { name: string; description: string }): Promise<void> {
  const overrides = readOverrides();
  overrides[sectorId] = { ...overrides[sectorId], ...patch };
  writeOverrides(overrides);
  return Promise.resolve();
}
