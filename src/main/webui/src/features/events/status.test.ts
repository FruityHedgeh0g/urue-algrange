import { describe, expect, it } from "vitest";
import { allowedTransitions, currentStatus } from "./status";

const NOW = new Date("2026-06-01T12:00:00");
const at = (start: string, end: string) => ({ startDateTime: start, endDateTime: end });

describe("currentStatus", () => {
  it("keeps the stored status before the start", () => {
    expect(currentStatus({ status: "ouvert", ...at("2026-06-02T09:00:00", "2026-06-02T18:00:00") }, NOW)).toBe("ouvert");
  });

  it("reads en_cours then archive for an Ouvert or Complet Event", () => {
    expect(currentStatus({ status: "complet", ...at("2026-06-01T09:00:00", "2026-06-01T18:00:00") }, NOW)).toBe("en_cours");
    expect(currentStatus({ status: "ouvert", ...at("2026-05-01T09:00:00", "2026-05-01T18:00:00") }, NOW)).toBe("archive");
  });

  it("leaves Planification and Annulé alone", () => {
    const past = at("2026-05-01T09:00:00", "2026-05-01T18:00:00");
    expect(currentStatus({ status: "planification", ...past }, NOW)).toBe("planification");
    expect(currentStatus({ status: "annule", ...past }, NOW)).toBe("annule");
  });
});

describe("allowedTransitions", () => {
  it.each([
    ["planification", ["ouvert", "annule"]],
    ["ouvert", ["complet", "annule"]],
    ["complet", ["ouvert", "annule"]],
    ["en_cours", ["annule"]],
    ["archive", []],
    ["annule", []],
  ] as const)("from %s", (from, expected) => {
    expect(allowedTransitions(from)).toEqual(expected);
  });
});
