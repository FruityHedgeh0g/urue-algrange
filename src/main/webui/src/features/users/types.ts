import { RoleId } from "../../auth/roles";

/**
 * Reflète NestedUserDto côté backend, complété d'informations de fiche
 * (rôle, contact, ancienneté). Ces derniers champs n'existent pas encore dans
 * UserDto côté backend (l'identité/contact viendrait de Keycloak) et restent
 * mockés en attendant.
 */
export interface Member {
  userId: string;
  firstName: string;
  lastName: string;
  role: RoleId;
  /** Secteur de la personne à partir de Membre (ADR 0004) ; aucun pour un Bénévole ou le Super admin. */
  sectorId?: string | null;
  /** Le membre du Bureau qui préside l'association (un seul à la fois). */
  president?: boolean;
  email: string;
  phone: string;
  memberSince: string; // date ISO
}
