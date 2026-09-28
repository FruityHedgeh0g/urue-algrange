import { mockMembers } from "./fixtures";
import { Member } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/**
 * Client mocké — le UserController backend n'expose que GET /api/users pour
 * l'instant (édition commentée). Même signature qu'un futur PATCH réel.
 */
export interface MemberInput {
  firstName: string;
  lastName: string;
  groupId: string;
}

export function createUsersApi(store: JsonStore = localJsonStore) {
  const members = createOverlayCollection<Member>({ store, name: "member", fixtures: mockMembers, idOf: (m) => m.userId });
  return {
    fetchAllMembers: () => members.list(),
    fetchMembersByGroupIds: async (groupIds: string[]) => (await members.list()).filter((m) => groupIds.includes(m.groupId)),
    updateMember: (userId: string, patch: MemberInput) => members.update(userId, patch),
  };
}

export const { fetchAllMembers, fetchMembersByGroupIds, updateMember } = createUsersApi();
