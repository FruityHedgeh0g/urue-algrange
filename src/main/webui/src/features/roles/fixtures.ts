import { Role } from "./types";

export const mockRoles: Role[] = [
  { roleId: "role-membre", name: "Membre", description: "Accès de base réservé aux adhérents de l'association.", roleType: "organizational_role" },
  { roleId: "role-benevole", name: "Bénévole", description: "Peut s'inscrire aux événements et participer aux actions de terrain.", roleType: "organizational_role" },
  { roleId: "role-chef-groupe", name: "Chef de groupe", description: "Gère les informations et les inscrits de son secteur.", roleType: "organizational_role" },
  { roleId: "role-bureau", name: "Bureau", description: "Gère les secteurs, les événements, les inscrits et les rôles courants.", roleType: "organizational_role" },
  { roleId: "role-tresorier", name: "Trésorier", description: "Responsable légal des finances de l'association.", roleType: "legal_role" },
  { roleId: "role-president", name: "Président", description: "Représentant légal de l'association.", roleType: "legal_role" },
  { roleId: "role-admin", name: "Admin", description: "Administration technique du site et de la configuration.", roleType: "organizational_role" },
];
