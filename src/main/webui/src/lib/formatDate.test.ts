import { describe, expect, it } from "vitest";
import { formatDate, formatDateRange } from "./formatDate";

describe("formatDate", () => {
  it("formats an ISO date in French long form", () => {
    expect(formatDate("2026-08-15T09:00:00")).toBe("15 août 2026");
  });
});

describe("formatDateRange", () => {
  it("formats a same-day range with start and end times", () => {
    expect(formatDateRange("2026-08-15T09:00:00", "2026-08-15T18:00:00")).toBe("15 août 2026 · 09:00 - 18:00");
  });

  it("formats a multi-day range with both dates", () => {
    expect(formatDateRange("2026-08-15T09:00:00", "2026-08-17T18:00:00")).toBe("Du 15 août 2026 au 17 août 2026");
  });
});
