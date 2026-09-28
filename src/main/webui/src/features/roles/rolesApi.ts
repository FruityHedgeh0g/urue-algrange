import { mockRoles } from "./fixtures";
import { Role } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké — le RoleController backend n'expose que GET /api/roles pour
 * l'instant (création/édition commentées). Mêmes signatures qu'un futur
 * POST/PATCH réel.
 */
export interface RoleInput {
  name: string;
  description: string;
  permissions: string[];
}

export function createRolesApi(store: JsonStore = localJsonStore) {
  const roles = createOverlayCollection<Role>({ store, name: "role", fixtures: mockRoles, idOf: (r) => r.roleId });
  return {
    fetchRoles: () => roles.list(),
    updateRole: (roleId: string, patch: RoleInput) => roles.update(roleId, patch),
    createRole: (input: RoleInput) => roles.create({ roleId: `role-${Date.now()}`, roleType: "organizational_role", ...input }),
  };
}

export const { fetchRoles, updateRole, createRole } = createRolesApi();
