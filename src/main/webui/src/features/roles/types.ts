/** Reflète RoleDto côté backend, complété des permissions (voir PERMISSION_FEATURES). */
export interface Role {
  roleId: string;
  name: string;
  description: string;
  roleType: "organizational_role" | "legal_role";
  permissions: string[];
}

/** Rôles à statut particulier : gestion réservée à l'Admin (cf. cas d'usage "Gérer les rôles (Sauf Président et Admin)"). */
export const PROTECTED_ROLE_NAMES = ["Admin", "Président"];

export interface PermissionFeature {
  id: string;
  label: string;
}

/**
 * Grandes fonctionnalités de l'espace Bureau/Admin qu'un rôle peut couvrir.
 * Purement descriptif pour l'instant : le contrôle d'accès réel de
 * l'application repose sur la hiérarchie de rôles (auth/roles.ts), le
 * backend n'ayant pas encore de modèle de permissions par fonctionnalité.
 */
export const PERMISSION_FEATURES: PermissionFeature[] = [
  { id: "eventRegistration", label: "S'inscrire aux événements" },
  { id: "mySector", label: "Gérer mon secteur" },
  { id: "members", label: "Gérer les inscrits" },
  { id: "sectors", label: "Gérer les secteurs" },
  { id: "events", label: "Gérer les événements" },
  { id: "roles", label: "Gérer les rôles" },
  { id: "featureRequests", label: "Créer des demandes de fonctionnalité" },
  { id: "configuration", label: "Gérer la configuration du site" },
  { id: "featureFlags", label: "Activer/désactiver des fonctionnalités" },
];
