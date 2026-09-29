/** Chef de groupe tel que renvoyé par l'API (NestedUserDto). */
export interface GroupChef {
  userId: string;
  firstName: string;
  lastName: string;
}

/**
 * Reflète GroupDto côté backend (vue Basic). `sectorId` est un ajout du mock :
 * côté backend, le rattachement se fait par PUT /api/sectors/{sectorId}/group/{groupId}.
 */
export interface Group {
  groupId: string;
  name: string;
  description: string;
  /** Partie du Secteur couverte par le Groupe. */
  area: string;
  sectorId: string;
  /** Chef de groupe de l'Affectation en cours ; null si le Groupe n'a pas de chef. */
  chef?: GroupChef | null;
}
