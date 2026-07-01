/**
 * Rôles applicatifs côté front, dérivés du diagramme de cas d'usage.
 * Provisoire : le backend modélise les rôles comme des entités nommées libres
 * (ROLES.name / roleType), à réconcilier avec ce référentiel une fois les
 * endpoints d'authentification et de gestion des rôles disponibles.
 */
export type RoleId =
  | "visiteur"
  | "membre"
  | "benevole"
  | "chef_de_groupe"
  | "bureau"
  | "admin";

export const ROLE_HIERARCHY: RoleId[] = [
  "visiteur",
  "membre",
  "benevole",
  "chef_de_groupe",
  "bureau",
  "admin",
];

export const ROLE_LABELS: Record<RoleId, string> = {
  visiteur: "Visiteur",
  membre: "Membre",
  benevole: "Bénévole",
  chef_de_groupe: "Chef de groupe",
  bureau: "Bureau",
  admin: "Admin",
};

/** true si `current` a un niveau d'accès au moins égal à `required`. */
export function roleAtLeast(current: RoleId, required: RoleId): boolean {
  return ROLE_HIERARCHY.indexOf(current) >= ROLE_HIERARCHY.indexOf(required);
}
