import { vi } from "vitest";
import { mockSectors } from "../features/sectors/fixtures";
import { Sector } from "../features/sectors/types";
import { mockGroups } from "../features/groups/fixtures";
import { Group } from "../features/groups/types";
import { mockMembers } from "../features/users/fixtures";
import { RoleId } from "../auth/roles";

/**
 * Un backend en mémoire derrière `fetch`, pour les tests de pages : chaque client passé de ses fixtures à
 * l'API (frontend: replace the mocked clients, #32) y ajoute ses routes. Remis à zéro avant chaque test
 * (test/setup.ts). Il ne refait que ce que les pages observent ; les règles elles-mêmes sont testées côté Java.
 */
interface State {
  viewer: { role: RoleId; sectorId: string | null };
  sectors: Sector[];
  groups: Group[];
}

let state: State;

/** Qui l'API croit connecté, et son Secteur ; test/testUser le règle. */
export function setFakeViewer(role: RoleId, sectorId: string | null = null) {
  state.viewer = { role, sectorId };
}

export function resetFakeApi() {
  state = {
    viewer: { role: "visiteur", sectorId: null },
    sectors: structuredClone(mockSectors).map((s) => ({ ...s, closed: s.closed ?? false })),
    groups: structuredClone(mockGroups),
  };
  vi.stubGlobal("fetch", vi.fn(handle));
}

let seeded = 0;

/** Ajoute un Groupe tel quel, sans les règles de l'API ; renvoie son id. */
export function seedGroup(group: Partial<Group> & Pick<Group, "name" | "sectorId">): string {
  const groupId = group.groupId ?? `group-seed-${++seeded}`;
  state.groups.push({ description: "", area: "", chef: null, ...group, groupId });
  return groupId;
}

const json = (body: unknown, status = 200) =>
  new Response(JSON.stringify(body), { status, headers: { "Content-Type": "application/json" } });
const empty = (status: number) => new Response(null, { status });

const isSuperAdmin = () => state.viewer.role === "super_admin";
const visibleSector = (s: Sector) => !s.closed || isSuperAdmin();

async function handle(input: RequestInfo | URL, init: RequestInit = {}): Promise<Response> {
  const url = new URL(typeof input === "string" ? input : input.toString(), "http://localhost");
  const method = (init.method ?? "GET").toUpperCase();
  const body = typeof init.body === "string" ? JSON.parse(init.body) : undefined;
  const parts = url.pathname.split("/").filter(Boolean).map(decodeURIComponent);

  if (parts[0] === "api" && parts[1] === "sectors") return sectors(method, parts.slice(2), body);
  if (parts[0] === "api" && parts[1] === "groups") return groups(method, parts.slice(2), body);
  return empty(404);
}

function sectors(method: string, [sectorId, action]: string[], body: any): Response {
  if (!sectorId) {
    if (method === "GET") return json(state.sectors.filter(visibleSector));
    if (method === "POST") {
      if (!isSuperAdmin()) return empty(403);
      if (state.sectors.some((s) => s.name === body.name)) return empty(409);
      const created: Sector = { sectorId: `sector-${state.sectors.length + 1}-new`, name: body.name, description: body.description ?? "", groups: [], closed: false };
      state.sectors.push(created);
      return json({ sectorId: created.sectorId, name: created.name, description: created.description });
    }
    if (method === "PATCH") {
      const sector = state.sectors.find((s) => s.sectorId === body.sectorId && visibleSector(s));
      if (!sector) return empty(404);
      if (sector.closed) return empty(400);
      if (body.name !== sector.name && !isSuperAdmin()) return empty(403);
      Object.assign(sector, { name: body.name, description: body.description });
      return json(sector);
    }
    return empty(405);
  }

  const sector = state.sectors.find((s) => s.sectorId === sectorId && visibleSector(s));
  if (!sector) return empty(404);
  if (method === "GET" && !action) return json(sector);
  if (method === "POST" && (action === "close" || action === "reopen")) {
    if (!isSuperAdmin()) return empty(403);
    sector.closed = action === "close";
    return json(sector);
  }
  return empty(405);
}

const sectorOf = (sectorId: string) => state.sectors.find((s) => s.sectorId === sectorId);
const visibleGroup = (g: Group) => !sectorOf(g.sectorId)?.closed || isSuperAdmin();

function groups(method: string, [groupId, action, userId]: string[], body: any): Response {
  if (!groupId) {
    if (method === "GET") return json(state.groups.filter(visibleGroup));
    if (method === "POST") {
      const sectorId = isSuperAdmin() ? body.sectorId : state.viewer.sectorId;
      if (!sectorId || (!isSuperAdmin() && body.sectorId && body.sectorId !== sectorId)) return empty(403);
      if (sectorOf(sectorId)?.closed) return empty(400);
      if (state.groups.some((g) => g.name === body.name)) return empty(409);
      const groupId = seedGroup({ name: body.name, description: body.description, area: body.area, sectorId });
      const { chef: _chef, ...created } = state.groups.find((g) => g.groupId === groupId)!;
      return json(created);
    }
    if (method === "PATCH") {
      const group = state.groups.find((g) => g.groupId === body.groupId && visibleGroup(g));
      if (!group) return empty(404);
      if (sectorOf(group.sectorId)?.closed) return empty(400);
      Object.assign(group, { name: body.name, description: body.description, area: body.area });
      return json(group);
    }
    return empty(405);
  }

  const group = state.groups.find((g) => g.groupId === groupId && visibleGroup(g));
  if (!group) return empty(404);
  if (action !== "chef") return empty(405);
  if (sectorOf(group.sectorId)?.closed) return empty(400);
  if (method === "DELETE") {
    group.chef = null;
    return json(group);
  }
  if (method === "PUT" && userId) {
    const member = mockMembers.find((m) => m.userId === userId);
    if (!member) return empty(404);
    // Le Chef quitte le Groupe qu'il menait
    state.groups.filter((g) => g.chef?.userId === userId).forEach((g) => (g.chef = null));
    group.chef = { userId, firstName: member.firstName, lastName: member.lastName };
    return json(group);
  }
  return empty(405);
}
