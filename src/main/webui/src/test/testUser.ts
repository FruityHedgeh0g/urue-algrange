import { CurrentUser, Profile } from "../auth/session";
import { RoleId, roleAtLeast } from "../auth/roles";

/**
 * La personne connectée dans les tests, donnée à <AuthProvider user={...}> : Jean Dupont, du Secteur
 * d'Algrange des fixtures de Membre à Admin, aucun pour un Bénévole ni le Super admin (ADR 0004).
 * null pour un Visiteur.
 */
export function testUser(role: RoleId | string, profile: Partial<Profile> = {}): CurrentUser | null {
  if (role === "visiteur") return null;
  const r = role as RoleId;
  return {
    userId: "mock-user",
    firstName: "Jean",
    lastName: "Dupont",
    phone: "06 12 34 56 78",
    ...profile,
    role: r,
    sector: roleAtLeast(r, "membre") && r !== "super_admin" ? { sectorId: "sector-1", name: "Secteur Algrange" } : null,
  };
}
