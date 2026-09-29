import { describe, expect, it } from "vitest";
import { ROLE_HIERARCHY, roleAtLeast } from "./roles";

describe("roleAtLeast", () => {
  it("ranks every Role on the ladder, lowest first", () => {
    expect(ROLE_HIERARCHY).toEqual(["visiteur", "benevole", "membre", "chef_de_groupe", "bureau", "admin", "super_admin"]);
  });

  it("allows a role to access its own level", () => {
    expect(roleAtLeast("membre", "membre")).toBe(true);
  });

  it("allows a higher role to access a lower requirement", () => {
    expect(roleAtLeast("admin", "membre")).toBe(true);
    expect(roleAtLeast("bureau", "chef_de_groupe")).toBe(true);
    expect(roleAtLeast("membre", "benevole")).toBe(true);
    expect(roleAtLeast("super_admin", "admin")).toBe(true);
  });

  it("denies a lower role access to a higher requirement", () => {
    expect(roleAtLeast("visiteur", "benevole")).toBe(false);
    expect(roleAtLeast("benevole", "membre")).toBe(false);
    expect(roleAtLeast("membre", "bureau")).toBe(false);
    expect(roleAtLeast("admin", "super_admin")).toBe(false);
  });
});
