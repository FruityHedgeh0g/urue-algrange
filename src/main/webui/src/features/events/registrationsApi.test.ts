import { afterEach, describe, expect, it } from "vitest";
import { createEventsApi } from "./eventsApi";
import { createRegistrationsApi, PhoneRequiredError } from "./registrationsApi";
import { EventStatus } from "./status";

const PHONE = "06 12 34 56 78";

const inHours = (hours: number) => new Date(Date.now() + hours * 3600_000).toISOString().slice(0, 19);

/** Creates an Event and brings it to `status` through the allowed transitions. */
const eventIn = async (status: EventStatus, startInHours = 24, endInHours = 48) => {
  const events = createEventsApi();
  const { eventId } = await events.createEvent({
    name: `Test ${status}`,
    description: "",
    sectorId: "sector-1",
    startDateTime: inHours(startInHours),
    endDateTime: inHours(endInHours),
  });
  const path: Record<EventStatus, EventStatus[]> = {
    planification: [],
    ouvert: ["ouvert"],
    complet: ["ouvert", "complet"],
    annule: ["annule"],
    en_cours: ["ouvert"],
    archive: ["ouvert"],
  };
  for (const step of path[status]) await events.changeStatus(eventId, step);
  return eventId;
};

describe("registrationsApi", () => {
  afterEach(() => localStorage.clear());

  it("gives a place on an Ouvert Event", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert");
    expect(await api.signUp(eventId, PHONE)).toMatchObject({ eventId, mode: "pilote", status: "participant" });
  });

  it("puts a sign-up for a Complet Event on the Liste d'attente", async () => {
    const api = createRegistrationsApi();
    expect(await api.signUp(await eventIn("complet"), PHONE)).toMatchObject({ status: "en_attente" });
  });

  it.each(["planification", "annule"] as const)("refuses sign-up in %s", async (status) => {
    const api = createRegistrationsApi();
    await expect(api.signUp(await eventIn(status), PHONE)).rejects.toThrow();
  });

  it("refuses sign-up once the Event is En cours", async () => {
    const api = createRegistrationsApi();
    await expect(api.signUp(await eventIn("en_cours", -1, 1), PHONE)).rejects.toThrow();
  });

  it("asks for a phone number first", async () => {
    const api = createRegistrationsApi();
    await expect(api.signUp(await eventIn("ouvert"), "  ")).rejects.toBeInstanceOf(PhoneRequiredError);
  });

  it("withdraws until the Event is archived", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert");
    await api.signUp(eventId, PHONE);
    await api.withdraw(eventId);
    expect(await api.fetchMyRegistrations()).toEqual([]);
  });
});
