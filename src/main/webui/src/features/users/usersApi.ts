import { mockMembers } from "./fixtures";
import { Member } from "./types";

const OVERRIDES_KEY = "urue-member-overrides";

/**
 * Client mocké — le UserController backend n'expose que GET /api/users pour
 * l'instant (édition commentée). Même signature qu'un futur PATCH réel.
 */
function readOverrides(): Record<string, Partial<Member>> {
  try {
    const saved = localStorage.getItem(OVERRIDES_KEY);
    return saved ? JSON.parse(saved) : {};
  } catch {
    return {};
  }
}

function writeOverrides(overrides: Record<string, Partial<Member>>) {
  try {
    localStorage.setItem(OVERRIDES_KEY, JSON.stringify(overrides));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

async function fetchAllRaw(): Promise<Member[]> {
  const overrides = readOverrides();
  return mockMembers.map((member) => ({ ...member, ...overrides[member.userId] }));
}

export async function fetchAllMembers(): Promise<Member[]> {
  return fetchAllRaw();
}

export async function fetchMembersByGroupIds(groupIds: string[]): Promise<Member[]> {
  const all = await fetchAllRaw();
  return all.filter((m) => groupIds.includes(m.groupId));
}

export async function updateMember(userId: string, patch: { firstName: string; lastName: string; groupId: string }): Promise<void> {
  const overrides = readOverrides();
  overrides[userId] = { ...overrides[userId], ...patch };
  writeOverrides(overrides);
  return Promise.resolve();
}
