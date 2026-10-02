import { afterEach, describe, expect, it } from "vitest";
import { createEventsApi } from "./eventsApi";
import { createRegistrationsApi, PhoneRequiredError } from "./registrationsApi";
import { EventStatus } from "./status";

const person = (userId: string, phone = "06 12 34 56 78") => ({ userId, firstName: "Test", lastName: userId, phone });
const ME = person("me");

const inHours = (hours: number) => new Date(Date.now() + hours * 3600_000).toISOString().slice(0, 19);

/** Creates an Event and brings it to `status` through the allowed transitions. */
const eventIn = async (status: EventStatus, options: { start?: number; end?: number; max?: number } = {}) => {
  const events = createEventsApi();
  const { eventId } = await events.createEvent({
    name: `Test ${status}`,
    description: "",
    sectorId: "sector-1",
    startDateTime: inHours(options.start ?? 24),
    endDateTime: inHours(options.end ?? 48),
    maxParticipants: options.max,
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

describe("registrationsApi: signing up", () => {
  afterEach(() => localStorage.clear());

  it("gives a place on an Ouvert Event", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert");
    expect(await api.signUp(eventId, ME)).toMatchObject({ eventId, mode: "pilote", status: "participant" });
  });

  it("puts a sign-up for a Complet Event on the Liste d'attente", async () => {
    const api = createRegistrationsApi();
    expect(await api.signUp(await eventIn("complet"), ME)).toMatchObject({ status: "en_attente" });
  });

  it("puts a sign-up at the maximum on the Liste d'attente", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert", { max: 1 });
    await api.signUp(eventId, person("first"));
    expect(await api.signUp(eventId, ME)).toMatchObject({ status: "en_attente" });
  });

  it.each(["planification", "annule"] as const)("refuses sign-up in %s", async (status) => {
    const api = createRegistrationsApi();
    await expect(api.signUp(await eventIn(status), ME)).rejects.toThrow();
  });

  it("refuses sign-up once the Event is En cours", async () => {
    const api = createRegistrationsApi();
    await expect(api.signUp(await eventIn("en_cours", { start: -1, end: 1 }), ME)).rejects.toThrow();
  });

  it("asks for a phone number first", async () => {
    const api = createRegistrationsApi();
    await expect(api.signUp(await eventIn("ouvert"), person("me", "  "))).rejects.toBeInstanceOf(PhoneRequiredError);
  });

  it("withdraws until the Event is archived", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert");
    await api.signUp(eventId, ME);
    await api.withdraw(eventId, "me");
    expect(await api.fetchMyRegistrations("me")).toEqual([]);
  });
});

describe("registrationsApi: the Bureau's roster", () => {
  afterEach(() => localStorage.clear());

  it("lists Participants and the Liste d'attente in sign-up order", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert", { max: 1 });
    for (const id of ["first", "second", "third"]) await api.signUp(eventId, person(id));

    const roster = await api.fetchRoster(eventId);
    expect(roster.participants.map((p) => p.personId)).toEqual(["first"]);
    expect(roster.waiting.map((p) => p.personId)).toEqual(["second", "third"]);
  });

  it("moves someone up only while under the maximum", async () => {
    const api = createRegistrationsApi();
    const eventId = await eventIn("ouvert", { max: 1 });
    await api.signUp(eventId, person("first"));
    await api.signUp(eventId, person("second"));

    await expect(api.promote(eventId, "second")).rejects.toThrow();
    await api.remove(eventId, "first");
    expect((await api.fetchRoster(eventId)).waiting.map((p) => p.personId)).toEqual(["second"]);

    await api.promote(eventId, "second");
    expect((await api.fetchRoster(eventId)).participants.map((p) => p.personId)).toEqual(["second"]);
  });
});
