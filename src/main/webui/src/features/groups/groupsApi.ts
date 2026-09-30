import { mockGroups } from "./fixtures";
import { Group, GroupChef } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";
import { closedSectorIds, refuseInClosedSector } from "../sectors/closedSectors";

/**
 * Client mocké, mêmes contrats que GroupController : GET/POST/PATCH /api/groups,
 * PUT /api/groups/{groupId}/chef/{userId} et DELETE /api/groups/{groupId}/chef.
 * Les Groupes d'un Secteur fermé ne sont vus que du Super admin et ne changent plus.
 */
export interface GroupInput {
  name: string;
  description: string;
  area: string;
  sectorId: string;
}

export function createGroupsApi(store: JsonStore = localJsonStore) {
  const groups = createOverlayCollection<Group>({ store, name: "group", fixtures: mockGroups, idOf: (g) => g.groupId });
  const refuseWhenClosed = async (groupId: string) => refuseInClosedSector(store, (await groups.get(groupId))?.sectorId);

  return {
    /** Tous les Groupes, pour les références internes (inscriptions, Affectations). */
    fetchGroups: () => groups.list(),
    /** Ce que voit la personne : sans les Groupes d'un Secteur fermé, sauf pour le Super admin. */
    fetchVisibleGroups: async (seesClosedSecteurs: boolean) => {
      const closed = seesClosedSecteurs ? new Set<string>() : await closedSectorIds(store);
      return (await groups.list()).filter((g) => !closed.has(g.sectorId));
    },
    createGroup: async (input: GroupInput) => {
      await refuseInClosedSector(store, input.sectorId);
      await groups.create({ groupId: `group-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`, ...input });
    },
    updateGroup: async (groupId: string, patch: GroupInput) => {
      await refuseWhenClosed(groupId);
      await groups.update(groupId, patch);
    },
    /** À la fermeture d'un Secteur : ses Groupes n'ont plus de Chef (qui garde son titre). */
    endAffectationsOfSector: async (sectorId: string) => {
      for (const group of (await groups.list()).filter((g) => g.sectorId === sectorId)) await groups.update(group.groupId, { chef: null });
    },
    /** Affectation : le Chef quitte le Groupe qu'il menait éventuellement. */
    setChef: async (groupId: string, chef: GroupChef) => {
      await refuseWhenClosed(groupId);
      for (const previous of (await groups.list()).filter((g) => g.chef?.userId === chef.userId && g.groupId !== groupId)) {
        await groups.update(previous.groupId, { chef: null });
      }
      await groups.update(groupId, { chef });
    },
    clearChef: async (groupId: string) => {
      await refuseWhenClosed(groupId);
      await groups.update(groupId, { chef: null });
    },
  };
}

export const { fetchGroups, fetchVisibleGroups, createGroup, updateGroup, setChef, clearChef } = createGroupsApi();
