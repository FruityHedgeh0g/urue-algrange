import { mockMembers } from "./fixtures";
import { Member } from "./types";
import { canLeadGroupe, RoleId } from "../../auth/roles";
import { clearChef, fetchGroups } from "../groups/groupsApi";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké — le UserController backend n'expose que GET /api/users pour
 * l'instant (édition commentée). Même signature qu'un futur PATCH réel.
 */
export interface MemberInput {
  firstName: string;
  lastName: string;
}

export function createUsersApi(store: JsonStore = localJsonStore) {
  const members = createOverlayCollection<Member>({ store, name: "member", fixtures: mockMembers, idOf: (m) => m.userId });
  return {
    fetchAllMembers: () => members.list(),
    updateMember: (userId: string, patch: MemberInput) => members.update(userId, patch),
    /** Même contrat que PUT /api/users/{userId}/role (chaîne de promotion vérifiée côté backend). */
    changeRole: async (userId: string, role: RoleId) => {
      await members.update(userId, role === "bureau" ? { role } : { role, president: false });
      // Comme le backend : perdre le titre de Chef de groupe met fin à l'Affectation
      if (!canLeadGroupe(role)) {
        const led = (await fetchGroups()).find((g) => g.chef?.userId === userId);
        if (led) await clearChef(led.groupId);
      }
    },
    /** Même contrat que PUT /api/users/{userId}/president : le Président précédent perd le titre. */
    appointPresident: async (userId: string) => {
      for (const previous of (await members.list()).filter((m) => m.president)) {
        await members.update(previous.userId, { president: false });
      }
      await members.update(userId, { president: true });
    },
  };
}

export const { fetchAllMembers, updateMember, changeRole, appointPresident } = createUsersApi();
