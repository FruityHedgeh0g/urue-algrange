/** Reflète NestedUserDto côté backend, complété du groupe d'appartenance pour l'affichage. */
export interface Member {
  userId: string;
  firstName: string;
  lastName: string;
  groupId: string;
}
