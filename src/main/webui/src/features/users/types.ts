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
  /** Le membre du Bureau qui préside l'association (un seul à la fois). */
  president?: boolean;
  email: string;
  phone: string;
  memberSince: string; // date ISO
}
