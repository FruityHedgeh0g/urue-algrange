import { mockMembers } from "./fixtures";
import { Member } from "./types";
import { canLeadGroupe, RoleId, roleAtLeast } from "../../auth/roles";
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

/** Qui promeut : son Rôle et son Secteur (aucun pour le Super admin). */
export interface PromotionActor {
  role: RoleId;
  sectorId: string | null;
}

/**
 * Secteur d'une personne après un changement de Rôle (ADR 0004), comme le
 * backend : sous le Super admin, on n'agit que sur les Bénévoles et les gens de
 * son Secteur, et l'on donne son propre Secteur ; le Super admin le nomme en
 * désignant un Admin ou en donnant un premier Secteur. Un Bénévole n'en a aucun.
 */
function sectorAfter(actor: PromotionActor, member: Member, role: RoleId, sectorId?: string): string | null {
  const current = member.sectorId ?? null;
  if (actor.role !== "super_admin") {
    if (sectorId) throw new Error("Seul le Super admin choisit un secteur.");
    if (!actor.sectorId) throw new Error("Vous n'appartenez à aucun secteur.");
    if (current && current !== actor.sectorId) throw new Error("Cette personne appartient à un autre secteur.");
    return roleAtLeast(role, "membre") ? actor.sectorId : null;
  }
  if (!roleAtLeast(role, "membre")) return null;
  if (role !== "admin" && current) {
    if (sectorId && sectorId !== current) throw new Error("Seule la désignation d'un Admin change le secteur d'une personne.");
    return current;
  }
  if (!sectorId) throw new Error("Choisissez le secteur.");
  return sectorId;
}

export function createUsersApi(store: JsonStore = localJsonStore) {
  const members = createOverlayCollection<Member>({ store, name: "member", fixtures: mockMembers, idOf: (m) => m.userId });
  return {
    fetchAllMembers: () => members.list(),
    updateMember: (userId: string, patch: MemberInput) => members.update(userId, patch),
    /** Même contrat que PUT /api/users/{userId}/role : `sectorId`, le Secteur que nomme le Super admin. */
    changeRole: async (userId: string, role: RoleId, actor: PromotionActor, sectorId?: string) => {
      const member = await members.get(userId);
      if (!member) throw new Error("Personne introuvable.");
      const nextSector = sectorAfter(actor, member, role, sectorId);
      const changesSector = Boolean(member.sectorId) && member.sectorId !== nextSector;
      await members.update(userId, role === "bureau" ? { role, sectorId: nextSector } : { role, sectorId: nextSector, president: false });
      // Comme le backend : perdre le titre de Chef de groupe, ou quitter son Secteur, met fin à l'Affectation
      if (!canLeadGroupe(role) || changesSector) {
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
