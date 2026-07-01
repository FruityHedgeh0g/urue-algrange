export interface SectorGroup {
  groupId: string;
  name: string;
}

/** Reflète SectorDto côté backend (vue Detailed). */
export interface Sector {
  sectorId: string;
  name: string;
  description: string;
  groups: SectorGroup[];
}
