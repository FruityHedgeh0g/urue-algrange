import { Sector, SectorGroup } from "./types";
import { apiFetch, HttpError } from "../../lib/http";

export interface SectorInput {
  name: string;
  description: string;
}

/** SectorDto : `groups` (vue Detailed) et `closed` manquent dans la réponse à une création. */
interface SectorDto {
  sectorId: string;
  name: string;
  description?: string | null;
  groups?: SectorGroup[] | null;
  closed?: boolean;
}

const toSector = (dto: SectorDto): Sector => ({
  sectorId: dto.sectorId,
  name: dto.name,
  description: dto.description ?? "",
  groups: dto.groups ?? [],
  closed: dto.closed ?? false,
});

const path = (sectorId: string) => `/api/sectors/${encodeURIComponent(sectorId)}`;

/**
 * Les Secteurs, sur SectorController. Seul le Super admin ouvre (POST), renomme (PATCH), ferme et rouvre
 * (POST /{sectorId}/close et /reopen) un Secteur ; le Bureau tient sa description. Un Secteur n'est jamais
 * supprimé (ADR 0003) : fermé, il est en lecture seule et l'API ne le montre qu'au Super admin, avec ses
 * Groupes et Événements. Lire les Secteurs ne demande pas d'être connecté.
 */
export async function fetchSectors(): Promise<Sector[]> {
  return (await apiFetch<SectorDto[]>("/api/sectors")).map(toSector);
}

/** undefined pour un Secteur inconnu, ou fermé quand on n'est pas le Super admin. */
export async function fetchSectorById(sectorId: string): Promise<Sector | undefined> {
  try {
    return toSector(await apiFetch<SectorDto>(path(sectorId)));
  } catch (error) {
    if (error instanceof HttpError && error.status === 404) return undefined;
    throw error;
  }
}

export async function updateSector(sectorId: string, patch: SectorInput): Promise<void> {
  await apiFetch("/api/sectors", { method: "PATCH", body: JSON.stringify({ sectorId, ...patch }) });
}

export async function createSector(input: SectorInput): Promise<Sector> {
  return toSector(await apiFetch<SectorDto>("/api/sectors", { method: "POST", body: JSON.stringify(input) }));
}

/** Ferme au lieu de supprimer : les Affectations prennent fin, les Événements non terminés sont Annulés (En cours : Archivé). */
export async function closeSector(sectorId: string): Promise<void> {
  await apiFetch(`${path(sectorId)}/close`, { method: "POST" });
}

/** Les Groupes reviennent sans Chef ; les Événements gardent leur statut. */
export async function reopenSector(sectorId: string): Promise<void> {
  await apiFetch(`${path(sectorId)}/reopen`, { method: "POST" });
}
