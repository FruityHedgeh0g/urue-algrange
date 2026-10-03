import { mockSectors } from "./fixtures";
import { Sector } from "./types";
import { JsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Les Secteurs fermés selon les fixtures, pour les clients encore mockés des Groupes et Événements.
 * Les Secteurs eux-mêmes viennent de l'API (sectorsApi) : à supprimer quand ces deux clients y passent
 * à leur tour (#35, #37).
 */
export async function closedSectorIds(store: JsonStore): Promise<Set<string>> {
  const sectors = createOverlayCollection<Sector>({ store, name: "sector", fixtures: mockSectors, idOf: (s) => s.sectorId });
  return new Set((await sectors.list()).filter((s) => s.closed).map((s) => s.sectorId));
}

/** Un Secteur fermé est en lecture seule. */
export async function refuseInClosedSector(store: JsonStore, sectorId: string | undefined) {
  if (sectorId && (await closedSectorIds(store)).has(sectorId)) throw new Error("Ce secteur est fermé : il est en lecture seule.");
}
