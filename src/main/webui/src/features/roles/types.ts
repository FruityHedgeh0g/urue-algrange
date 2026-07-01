/** Reflète RoleDto côté backend. */
export interface Role {
  roleId: string;
  name: string;
  description: string;
  roleType: "organizational_role" | "legal_role";
}

/** Rôles à statut particulier : gestion réservée à l'Admin (cf. cas d'usage "Gérer les rôles (Sauf Président et Admin)"). */
export const PROTECTED_ROLE_NAMES = ["Admin", "Président"];
