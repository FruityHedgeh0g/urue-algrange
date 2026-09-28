import { mockSectors } from "./fixtures";
import { Sector } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké — le backend a un SectorController mais les endpoints de
 * lecture par id, création et édition sont commentés côté Java. Les
 * modifications sont donc persistées en localStorage en attendant, avec les
 * mêmes signatures qu'un futur GET/POST/PATCH /api/sectors.
 */
export interface SectorInput {
  name: string;
  description: string;
}

export function createSectorsApi(store: JsonStore = localJsonStore) {
  const sectors = createOverlayCollection<Sector>({ store, name: "sector", fixtures: mockSectors, idOf: (s) => s.sectorId });
  return {
    fetchSectors: () => sectors.list(),
    fetchSectorById: (sectorId: string) => sectors.get(sectorId),
    updateSector: (sectorId: string, patch: SectorInput) => sectors.update(sectorId, patch),
    createSector: (input: SectorInput) => sectors.create({ sectorId: `sector-${Date.now()}`, groups: [], ...input }),
    deleteSector: (sectorId: string) => sectors.remove(sectorId),
  };
}

export const { fetchSectors, fetchSectorById, updateSector, createSector, deleteSector } = createSectorsApi();
