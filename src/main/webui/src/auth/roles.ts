/**
 * Rôle unique et hiérarchisé de chaque personne (ADR 0001), du plus bas au
 * plus haut : un rôle accorde tout ce qu'accordent les rôles inférieurs.
 * Les identifiants sont ceux renvoyés par l'API (champ `role` de /api/users/me).
 */
export type RoleId =
  | "visiteur"
  | "benevole"
  | "membre"
  | "chef_de_groupe"
  | "bureau"
  | "admin"
  | "super_admin";

export const ROLE_HIERARCHY: RoleId[] = [
  "visiteur",
  "benevole",
  "membre",
  "chef_de_groupe",
  "bureau",
  "admin",
  "super_admin",
];

export const ROLE_LABELS: Record<RoleId, string> = {
  visiteur: "Visiteur",
  benevole: "Bénévole",
  membre: "Membre",
  chef_de_groupe: "Chef de groupe",
  bureau: "Bureau",
  admin: "Admin",
  super_admin: "Super admin",
};

/** true si `current` a un niveau d'accès au moins égal à `required`. */
export function roleAtLeast(current: RoleId, required: RoleId): boolean {
  return ROLE_HIERARCHY.indexOf(current) >= ROLE_HIERARCHY.indexOf(required);
}

/**
 * Rôles qu'`actor` peut donner à une personne qui a `current` (chaîne de
 * promotion, miroir de RoleEnum.maySetRole) : à partir du Bureau, une personne
 * inscrite placée sous l'acteur passe de `benevole` au rôle juste sous le sien. Vide si
 * l'acteur ne peut pas modifier ce rôle. Personne ne change son propre rôle.
 */
export function assignableRoles(actor: RoleId, current: RoleId): RoleId[] {
  const below = (role: RoleId) => roleAtLeast(role, "benevole") && !roleAtLeast(role, actor);
  if (!roleAtLeast(actor, "bureau") || !below(current)) return [];
  return ROLE_HIERARCHY.filter(below);
}
