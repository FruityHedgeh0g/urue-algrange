import { afterEach, describe, expect, it } from "vitest";
import { createEventsApi } from "./eventsApi";
import { createRegistrationsApi } from "./registrationsApi";

const inDays = (days: number) => new Date(Date.now() + days * 86_400_000).toISOString().slice(0, 19);

describe("registrationsApi: Membres ride only at their own Secteur's Events (ADR 0004)", () => {
  afterEach(() => localStorage.clear());

  it("refuses a Membre of another Secteur, not a Bénévole", async () => {
    const events = createEventsApi();
    const thionville = await events.createEvent({ name: "Test Thionville", description: "", sectorId: "sector-2", startDateTime: inDays(5), endDateTime: inDays(6) });
    await events.changeStatus(thionville.eventId, "ouvert");
    const api = createRegistrationsApi();

    const membre = { userId: "membre", firstName: "Test", lastName: "Membre", phone: "06 00 00 00 00", sectorId: "sector-1" };
    await expect(api.signUp(thionville.eventId, membre)).rejects.toThrow(/réservé/i);

    const benevole = { userId: "benevole", firstName: "Test", lastName: "Bénévole", phone: "06 00 00 00 00", sectorId: null };
    expect((await api.signUp(thionville.eventId, benevole)).status).toBe("participant");
  });
});
