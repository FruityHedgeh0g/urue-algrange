import { describe, expect, it } from "vitest";
import { roleAtLeast } from "./roles";

describe("roleAtLeast", () => {
  it("allows a role to access its own level", () => {
    expect(roleAtLeast("membre", "membre")).toBe(true);
  });

  it("allows a higher role to access a lower requirement", () => {
    expect(roleAtLeast("admin", "membre")).toBe(true);
    expect(roleAtLeast("bureau", "chef_de_groupe")).toBe(true);
  });

  it("denies a lower role access to a higher requirement", () => {
    expect(roleAtLeast("visiteur", "membre")).toBe(false);
    expect(roleAtLeast("membre", "bureau")).toBe(false);
  });
});
