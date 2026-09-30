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
let elsewhere: string;

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

describe("registrationsApi: Groupe maximum and the Groupe's Liste d'attente", () => {
  beforeEach(async () => {
    const events = createEventsApi();
    const groups = createGroupsApi();
    await groups.createGroup({ name: "Test Nord", description: "", area: "", sectorId: "sector-1" });
    await groups.createGroup({ name: "Test Ailleurs", description: "", area: "", sectorId: "sector-2" });
    const all = await groups.fetchGroups();
    nord = all.find((g) => g.name === "Test Nord")!.groupId;
    elsewhere = all.find((g) => g.name === "Test Ailleurs")!.groupId;
    await groups.setChef(nord, { userId: "chef-nord", firstName: "Chef", lastName: "Nord" });

    const event = await events.createEvent({ name: "Test Balade", description: "", sectorId: "sector-1", startDateTime: inDays(10), endDateTime: inDays(11) });
    await events.changeStatus(event.eventId, "ouvert");
    eventId = event.eventId;
  });

  afterEach(() => localStorage.clear());

  const nordIn = (roster: { groups: { group: { groupId: string } }[] }) => roster.groups.find((g) => g.group.groupId === nord)!;

  it("lets the Bureau set and clear a Groupe's maximum, only for a Groupe of the Event's Secteur", async () => {
    const api = createRegistrationsApi();
    expect(nordIn(await api.setGroupMaximum(eventId, nord, 2))).toMatchObject({ maximum: 2 });
    expect(nordIn(await api.fetchRoster(eventId))).toMatchObject({ maximum: 2 });
    expect(nordIn(await api.setGroupMaximum(eventId, nord, null))).toMatchObject({ maximum: null });
    await expect(api.setGroupMaximum(eventId, elsewhere, 2)).rejects.toThrow();
  });

  it("refuses to accept or place beyond the maximum; the Demande stays pending", async () => {
    const api = createRegistrationsApi();
    await api.setGroupMaximum(eventId, nord, 1);
    await api.signUp(eventId, person("anne"), nord);
    await api.signUp(eventId, person("bruno"), nord);
    await api.decideDemande(eventId, "anne", CHEF, true);

    await expect(api.decideDemande(eventId, "bruno", CHEF, true)).rejects.toThrow(/maximum/);
    await expect(api.placeInGroup(eventId, "bruno", nord)).rejects.toThrow(/maximum/);
    expect(nordIn(await api.fetchRoster(eventId)).demandes.map((d) => d.personId)).toEqual(["bruno"]);
  });

  it("shows the Groupe's pending Demandes in sign-up order, in Mon groupe and the Bureau roster", async () => {
    const api = createRegistrationsApi();
    await api.setGroupMaximum(eventId, nord, 1);
    for (const id of ["claire", "anne", "bruno"]) await api.signUp(eventId, person(id), nord);

    const balade = (await api.fetchMonGroupe("chef-nord")).events.find((e) => e.eventId === eventId)!;
    expect(balade.maximum).toBe(1);
    expect(balade.demandes.map((d) => d.personId)).toEqual(["claire", "anne", "bruno"]);
    expect(nordIn(await api.fetchRoster(eventId)).demandes.map((d) => d.personId)).toEqual(["claire", "anne", "bruno"]);
  });

  it("frees a place when someone is taken out, without accepting anyone", async () => {
    const api = createRegistrationsApi();
    await api.setGroupMaximum(eventId, nord, 1);
    for (const id of ["anne", "bruno", "claire"]) await api.signUp(eventId, person(id), nord);
    await api.decideDemande(eventId, "anne", CHEF, true);

    await api.takeOutOfGroup(eventId, "anne", CHEF);
    const nordAfter = nordIn(await api.fetchRoster(eventId));
    expect(nordAfter.members).toEqual([]);
    expect(nordAfter.demandes.map((d) => d.personId)).toEqual(["bruno", "claire"]);

    expect((await api.decideDemande(eventId, "claire", CHEF, true)).group?.groupId).toBe(nord);
    await expect(api.decideDemande(eventId, "bruno", CHEF, true)).rejects.toThrow();
  });
});
