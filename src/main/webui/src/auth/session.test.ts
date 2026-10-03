import { describe, expect, it } from "vitest";
import { loginUrl } from "./session";

describe("loginUrl", () => {
  it("comes back to the page asked for", () => {
    expect(loginUrl("/evenements/42?tab=roster")).toBe("/api/auth/login?redirect=%2Fevenements%2F42%3Ftab%3Droster");
  });

  it("opens Keycloak's registration form", () => {
    expect(loginUrl("/", true)).toBe("/api/auth/login?redirect=%2F&prompt=create");
  });
});
