import { RoleId } from "../../auth/roles";

/** Une personne de la liste des Inscrits : reflète UserDto côté backend (vue Basic). */
export interface Member {
  userId: string;
  firstName: string;
  lastName: string;
  role: RoleId;
  /** Secteur de la personne à partir de Membre (ADR 0004) ; aucun pour un Bénévole ou le Super admin. */
  sectorId?: string | null;
  /** Le membre du Bureau qui préside l'association (un seul à la fois). */
  president?: boolean;
}
