/**
 * Reflète NestedUserDto côté backend, complété du groupe d'appartenance et
 * d'informations de fiche (rôle, contact, ancienneté). Ces derniers champs
 * n'existent pas encore dans UserDto côté backend (l'identité/contact
 * viendrait de Keycloak) et restent mockés en attendant.
 */
export interface Member {
  userId: string;
  firstName: string;
  lastName: string;
  groupId: string;
  role: string;
  email: string;
  phone: string;
  memberSince: string; // date ISO
}
