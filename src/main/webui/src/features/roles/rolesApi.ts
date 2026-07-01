import { mockRoles } from "./fixtures";
import { Role } from "./types";

const OVERRIDES_KEY = "urue-role-overrides";
const CREATED_KEY = "urue-role-created";

/**
 * Client mocké — le RoleController backend n'expose que GET /api/roles pour
 * l'instant (création/édition commentées). Mêmes signatures qu'un futur
 * POST/PATCH réel.
 */
function readJson<T>(key: string, fallback: T): T {
  try {
    const saved = localStorage.getItem(key);
    return saved ? JSON.parse(saved) : fallback;
  } catch {
    return fallback;
  }
}

function writeJson(key: string, value: unknown) {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

export interface RoleInput {
  name: string;
  description: string;
  permissions: string[];
}

export async function fetchRoles(): Promise<Role[]> {
  const overrides = readJson<Record<string, Partial<Role>>>(OVERRIDES_KEY, {});
  const created = readJson<Role[]>(CREATED_KEY, []);
  const base = mockRoles.map((role) => ({ ...role, ...overrides[role.roleId] }));
  return Promise.resolve([...base, ...created]);
}

export async function updateRole(roleId: string, patch: RoleInput): Promise<void> {
  const overrides = readJson<Record<string, Partial<Role>>>(OVERRIDES_KEY, {});
  overrides[roleId] = { ...overrides[roleId], ...patch };
  writeJson(OVERRIDES_KEY, overrides);
  return Promise.resolve();
}

export async function createRole(input: RoleInput): Promise<void> {
  const created = readJson<Role[]>(CREATED_KEY, []);
  created.push({ roleId: `role-${Date.now()}`, roleType: "organizational_role", ...input });
  writeJson(CREATED_KEY, created);
  return Promise.resolve();
}
