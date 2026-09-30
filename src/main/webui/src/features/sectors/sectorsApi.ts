import { mockSectors } from "./fixtures";
import { Sector } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";
import { createEventsApi } from "../events/eventsApi";
import { createGroupsApi } from "../groups/groupsApi";

export interface SectorInput {
  name: string;
  description: string;
}

/**
 * Client mocké, mêmes contrats que SectorController : seul le Super admin
 * ouvre (POST), renomme (PATCH avec `mayRename`), ferme et rouvre
 * (POST /{sectorId}/close et /reopen) un Secteur ; le Bureau tient sa
 * description. Un Secteur n'est jamais supprimé (ADR 0003) : fermé, il est en
 * lecture seule et vu, avec ses Groupes et Événements, du seul Super admin
 * (`seesClosed`).
 */
export function createSectorsApi(store: JsonStore = localJsonStore) {
  const sectors = createOverlayCollection<Sector>({ store, name: "sector", fixtures: mockSectors, idOf: (s) => s.sectorId });
  const visible = (seesClosed: boolean) => (sector: Sector) => seesClosed || !sector.closed;

  const sectorOrThrow = async (sectorId: string) => {
    const sector = await sectors.get(sectorId);
    if (!sector) throw new Error("Secteur introuvable.");
    return sector;
  };

  return {
    fetchSectors: async (seesClosed: boolean) => (await sectors.list()).filter(visible(seesClosed)),
    fetchSectorById: async (sectorId: string, seesClosed: boolean) => {
      const sector = await sectors.get(sectorId);
      return sector && visible(seesClosed)(sector) ? sector : undefined;
    },
    updateSector: async (sectorId: string, patch: SectorInput, mayRename: boolean) => {
      const sector = await sectorOrThrow(sectorId);
      if (sector.closed) throw new Error("Ce secteur est fermé : il est en lecture seule.");
      if (patch.name !== sector.name && !mayRename) throw new Error("Seul le Super admin renomme un secteur.");
      await sectors.update(sectorId, patch);
    },
    createSector: async (input: SectorInput): Promise<Sector> => {
      const sector: Sector = { sectorId: `sector-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`, groups: [], closed: false, ...input };
      await sectors.create(sector);
      return sector;
    },
    /** Ferme au lieu de supprimer : les Affectations prennent fin, les Événements non terminés sont Annulés (En cours : Archivé). */
    closeSector: async (sectorId: string) => {
      const sector = await sectorOrThrow(sectorId);
      if (sector.closed) return;
      await createGroupsApi(store).endAffectationsOfSector(sectorId);
      await createEventsApi(store).closeEventsOfSector(sectorId);
      await sectors.update(sectorId, { closed: true });
    },
    /** Les Groupes reviennent sans Chef ; les Événements gardent leur statut. */
    reopenSector: async (sectorId: string) => {
      await sectorOrThrow(sectorId);
      await sectors.update(sectorId, { closed: false });
    },
  };
}

export const { fetchSectors, fetchSectorById, updateSector, createSector, closeSector, reopenSector } = createSectorsApi();
