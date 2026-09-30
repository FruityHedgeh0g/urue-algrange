import { mockGroups } from "./fixtures";
import { Group, GroupChef } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké, mêmes contrats que GroupController : GET/POST/PATCH /api/groups,
 * PUT /api/groups/{groupId}/chef/{userId} et DELETE /api/groups/{groupId}/chef.
 */
export interface GroupInput {
  name: string;
  description: string;
  area: string;
  sectorId: string;
}

export function createGroupsApi(store: JsonStore = localJsonStore) {
  const groups = createOverlayCollection<Group>({ store, name: "group", fixtures: mockGroups, idOf: (g) => g.groupId });
  return {
    fetchGroups: () => groups.list(),
    createGroup: (input: GroupInput) => groups.create({ groupId: `group-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`, ...input }),
    updateGroup: (groupId: string, patch: GroupInput) => groups.update(groupId, patch),
    /** Affectation : le Chef quitte le Groupe qu'il menait éventuellement. */
    setChef: async (groupId: string, chef: GroupChef) => {
      for (const previous of (await groups.list()).filter((g) => g.chef?.userId === chef.userId && g.groupId !== groupId)) {
        await groups.update(previous.groupId, { chef: null });
      }
      await groups.update(groupId, { chef });
    },
    clearChef: (groupId: string) => groups.update(groupId, { chef: null }),
  };
}

export const { fetchGroups, createGroup, updateGroup, setChef, clearChef } = createGroupsApi();
