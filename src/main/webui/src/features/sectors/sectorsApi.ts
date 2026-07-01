import { mockSectors } from "./fixtures";
import { Sector } from "./types";

const OVERRIDES_KEY = "urue-sector-overrides";
const CREATED_KEY = "urue-sector-created";

/**
 * Client mocké — le backend a un SectorController mais les endpoints de
 * lecture par id, création et édition sont commentés côté Java. Les
 * modifications sont donc persistées en localStorage en attendant, avec les
 * mêmes signatures qu'un futur GET/POST/PATCH /api/sectors.
 */
function readJson<T>(key: string, fallback: T): T {
  try {
    const saved = localStorage.getItem(key);
    return saved ? JSON.parse(saved) : fallback;
  } catch {
    return fallback;
  }
}

function writeJson(key: string, value: unknown) {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

function readOverrides(): Record<string, Partial<Sector>> {
  return readJson(OVERRIDES_KEY, {});
}

async function fetchAllRaw(): Promise<Sector[]> {
  const overrides = readOverrides();
  const created = readJson<Sector[]>(CREATED_KEY, []);
  const base = mockSectors.map((sector) => ({ ...sector, ...overrides[sector.sectorId] }));
  return [...base, ...created];
}

export async function fetchSectors(): Promise<Sector[]> {
  return fetchAllRaw();
}

export async function fetchSectorById(sectorId: string): Promise<Sector | undefined> {
  const all = await fetchAllRaw();
  return all.find((s) => s.sectorId === sectorId);
}

export async function updateSector(sectorId: string, patch: { name: string; description: string }): Promise<void> {
  const overrides = readOverrides();
  overrides[sectorId] = { ...overrides[sectorId], ...patch };
  writeJson(OVERRIDES_KEY, overrides);
  return Promise.resolve();
}

export async function createSector(input: { name: string; description: string }): Promise<void> {
  const created = readJson<Sector[]>(CREATED_KEY, []);
  created.push({ sectorId: `sector-${Date.now()}`, groups: [], ...input });
  writeJson(CREATED_KEY, created);
  return Promise.resolve();
}
