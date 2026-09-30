import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { createEventsApi } from "./eventsApi";
import { createRegistrationsApi } from "./registrationsApi";
import { createGroupsApi } from "../groups/groupsApi";

const person = (userId: string) => ({ userId, firstName: "Test", lastName: userId, phone: "06 12 34 56 78" });
const CHEF = { personId: "chef-nord", bureau: false };
const OTHER_CHEF = { personId: "chef-sud", bureau: false };
const BUREAU = { personId: "bureau", bureau: true };

let eventId: string;
let nord: string;
let sud: string;

const inDays = (days: number) => new Date(Date.now() + days * 86_400_000).toISOString().slice(0, 19);

describe("registrationsApi: Demande de groupe", () => {
  beforeEach(async () => {
    const events = createEventsApi();
    const groups = createGroupsApi();
    const base = { description: "", area: "", sectorId: "sector-1" };
    await groups.createGroup({ ...base, name: "Test Nord" });
    await groups.createGroup({ ...base, name: "Test Sud" });
    const all = await groups.fetchGroups();
    nord = all.find((g) => g.name === "Test Nord")!.groupId;
    sud = all.find((g) => g.name === "Test Sud")!.groupId;
    await groups.setChef(nord, { userId: "chef-nord", firstName: "Chef", lastName: "Nord" });
    await groups.setChef(sud, { userId: "chef-sud", firstName: "Chef", lastName: "Sud" });

    const event = await events.createEvent({ name: "Test Balade", description: "", sectorId: "sector-1", startDateTime: inDays(10), endDateTime: inDays(11) });
    await events.changeStatus(event.eventId, "ouvert");
    eventId = event.eventId;
  });

  afterEach(() => localStorage.clear());

  it("creates a pending Demande when signing up with a Groupe", async () => {
    const api = createRegistrationsApi();
    const registration = await api.signUp(eventId, person("pilote"), nord);
    expect(registration.group).toBeNull();
    expect(registration.demande).toMatchObject({ group: { groupId: nord, name: "Test Nord" }, status: "en_attente" });
  });

  it("lets the Groupe's Chef or the Bureau decide, not another Chef", async () => {
    const api = createRegistrationsApi();
    await api.signUp(eventId, person("pilote"), nord);

    await expect(api.decideDemande(eventId, "pilote", OTHER_CHEF, true)).rejects.toThrow();
    expect((await api.decideDemande(eventId, "pilote", CHEF, true)).group?.groupId).toBe(nord);

    await api.signUp(eventId, person("autre"), sud);
    expect((await api.decideDemande(eventId, "autre", BUREAU, true)).group?.groupId).toBe(sud);
  });

  it("leaves a refused Participant without a Groupe, free to ask again", async () => {
    const api = createRegistrationsApi();
    await api.signUp(eventId, person("pilote"), nord);
    const refused = await api.decideDemande(eventId, "pilote", CHEF, false);
    expect(refused).toMatchObject({ status: "participant", group: null, demande: { status: "refusee" } });

    const again = await api.requestGroup(eventId, "pilote", sud);
    expect(again.demande).toMatchObject({ group: { groupId: sud }, status: "en_attente" });
  });

  it("lets the Bureau place a Participant, and the Chef take them out", async () => {
    const api = createRegistrationsApi();
    await api.signUp(eventId, person("pilote"));
    const roster = await api.placeInGroup(eventId, "pilote", nord);
    expect(roster.participants[0].group?.groupId).toBe(nord);

    await expect(api.takeOutOfGroup(eventId, "pilote", OTHER_CHEF)).rejects.toThrow();
    expect(await api.takeOutOfGroup(eventId, "pilote", CHEF)).toMatchObject({ status: "participant", group: null });
  });

  it("builds Mon groupe per Event, or nothing without an Affectation", async () => {
    const api = createRegistrationsApi();
    await api.signUp(eventId, person("pilote"), nord);

    const mine = await api.fetchMonGroupe("chef-nord");
    expect(mine.group?.groupId).toBe(nord);
    const balade = mine.events.find((e) => e.eventId === eventId)!;
    expect(balade.demandes.map((d) => d.personId)).toEqual(["pilote"]);
    expect(balade.members).toEqual([]);

    expect(await api.fetchMonGroupe("nobody")).toEqual({ group: null, events: [] });
  });
});
