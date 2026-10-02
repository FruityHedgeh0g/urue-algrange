import { describe, expect, it } from "vitest";
import { assignableRoles, ROLE_HIERARCHY, roleAtLeast } from "./roles";

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

describe("assignableRoles", () => {
  it("lets the Bureau move membre and chef_de_groupe", () => {
    expect(assignableRoles("bureau", "benevole")).toEqual(["benevole", "membre", "chef_de_groupe"]);
    expect(assignableRoles("bureau", "chef_de_groupe")).toEqual(["benevole", "membre", "chef_de_groupe"]);
  });

  it("lets an Admin grant bureau and the Super admin grant admin", () => {
    expect(assignableRoles("admin", "membre")).toEqual(["benevole", "membre", "chef_de_groupe", "bureau"]);
    expect(assignableRoles("super_admin", "admin")).toEqual(["benevole", "membre", "chef_de_groupe", "bureau", "admin"]);
  });

  it("offers nothing on a person at or above the viewer's level", () => {
    expect(assignableRoles("bureau", "bureau")).toEqual([]);
    expect(assignableRoles("admin", "super_admin")).toEqual([]);
  });

  it("offers nothing below the Bureau", () => {
    expect(assignableRoles("chef_de_groupe", "benevole")).toEqual([]);
    expect(assignableRoles("membre", "benevole")).toEqual([]);
  });

  it("offers nothing on a visiteur, who holds no Role to change", () => {
    expect(assignableRoles("bureau", "visiteur")).toEqual([]);
  });
});
