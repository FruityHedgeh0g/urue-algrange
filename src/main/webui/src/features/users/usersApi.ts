import { mockMembers } from "./fixtures";
import { Member } from "./types";

/** Client mocké — même signature qu'un futur GET /api/users. */
export async function fetchMembersByGroupIds(groupIds: string[]): Promise<Member[]> {
  return Promise.resolve(mockMembers.filter((m) => groupIds.includes(m.groupId)));
}
