import { Sector } from "./types";

export const mockSectors: Sector[] = [
  {
    sectorId: "sector-1",
    name: "Secteur Algrange",
    description: "Couvre Algrange et les communes limitrophes du bassin d'Algrange.",
    groups: [{ groupId: "group-1", name: "Groupe Algrange Centre" }],
  },
  {
    sectorId: "sector-2",
    name: "Secteur Thionville",
    description: "Couvre l'agglomération de Thionville.",
    groups: [{ groupId: "group-2", name: "Groupe Thionville" }],
  },
];
