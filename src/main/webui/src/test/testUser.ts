import { CurrentUser, Profile } from "../auth/session";
import { RoleId, roleAtLeast } from "../auth/roles";
import { setFakeViewer } from "./fakeApi";

/**
 * La personne connectée dans les tests, donnée à <AuthProvider user={...}> : Jean Dupont, du Secteur
 * d'Algrange des fixtures de Membre à Admin, aucun pour un Bénévole ni le Super admin (ADR 0004).
 * null pour un Visiteur. C'est aussi la personne que le faux backend (test/fakeApi) croit connectée.
 */
export function testUser(role: RoleId | string, profile: Partial<Profile> = {}): CurrentUser | null {
  const r = role as RoleId;
  const sector = roleAtLeast(r, "membre") && r !== "super_admin" ? { sectorId: "sector-1", name: "Secteur Algrange" } : null;
  setFakeViewer(r, sector?.sectorId ?? null);
  if (r === "visiteur") return null;
  return {
    userId: "mock-user",
    firstName: "Jean",
    lastName: "Dupont",
    phone: "06 12 34 56 78",
    ...profile,
    role: r,
    sector,
  };
}
