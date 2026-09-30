import { afterEach, describe, expect, it } from "vitest";
import { createUsersApi } from "./usersApi";

const BUREAU_ALGRANGE = { role: "bureau" as const, sectorId: "sector-1" };
const SUPER_ADMIN = { role: "super_admin" as const, sectorId: null };

const sectorOf = async (userId: string) => (await createUsersApi().fetchAllMembers()).find((m) => m.userId === userId)?.sectorId ?? null;

describe("usersApi: a person's Secteur, set by promotion (ADR 0004)", () => {
  afterEach(() => localStorage.clear());

  it("gives a new Membre the promoter's Secteur, and takes it back on demotion", async () => {
    const api = createUsersApi();
    await api.changeRole("user-2", "membre", BUREAU_ALGRANGE);
    expect(await sectorOf("user-2")).toBe("sector-1");

    await api.changeRole("user-2", "benevole", BUREAU_ALGRANGE);
    expect(await sectorOf("user-2")).toBeNull();
  });

  it("refuses a Bureau member acting on another Secteur's people or naming a Secteur", async () => {
    const api = createUsersApi();
    await expect(api.changeRole("user-6", "membre", BUREAU_ALGRANGE)).rejects.toThrow();
    await expect(api.changeRole("user-2", "membre", BUREAU_ALGRANGE, "sector-2")).rejects.toThrow();
  });

  it("lets the Super admin appoint an Admin from anyone, for the Secteur they name", async () => {
    const api = createUsersApi();
    await expect(api.changeRole("user-2", "admin", SUPER_ADMIN)).rejects.toThrow();
    await api.changeRole("user-4", "admin", SUPER_ADMIN, "sector-2");
    expect(await sectorOf("user-4")).toBe("sector-2");
  });
});
