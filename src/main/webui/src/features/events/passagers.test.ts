import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { createEventsApi } from "./eventsApi";
import { createRegistrationsApi } from "./registrationsApi";
import { createGroupsApi } from "../groups/groupsApi";

const person = (userId: string) => ({ userId, firstName: "Test", lastName: userId, phone: "06 12 34 56 78" });
const CHEF = { personId: "chef-nord", bureau: false };

let eventId: string;
let nord: string;

const inDays = (days: number) => new Date(Date.now() + days * 86_400_000).toISOString().slice(0, 19);

const setMaximum = async (maxParticipants: number) => {
  const event = (await createEventsApi().fetchEventById(eventId, true))!;
  await createEventsApi().updateEvent(eventId, { ...event, description: event.description ?? "", maxParticipants });
};

describe("registrationsApi: passagers", () => {
  beforeEach(async () => {
    const events = createEventsApi();
    const groups = createGroupsApi();
    await groups.createGroup({ name: "Test Nord", description: "", area: "", sectorId: "sector-1" });
    nord = (await groups.fetchGroups()).find((g) => g.name === "Test Nord")!.groupId;
    await groups.setChef(nord, { userId: "chef-nord", firstName: "Chef", lastName: "Nord" });

    const event = await events.createEvent({ name: "Test Balade", description: "", sectorId: "sector-1", startDateTime: inDays(10), endDateTime: inDays(11) });
    await events.changeStatus(event.eventId, "ouvert");
    eventId = event.eventId;
  });

  afterEach(() => localStorage.clear());

  it("signs a passager up with a pilote of the same Event, following their Groupe", async () => {
    const api = createRegistrationsApi();
    await expect(api.signUp(eventId, person("passager"), undefined, "pilote")).rejects.toThrow();

    await api.signUp(eventId, person("pilote"), nord);
    await api.decideDemande(eventId, "pilote", CHEF, true);
    const passager = await api.signUp(eventId, person("passager"), undefined, "pilote");
    expect(passager).toMatchObject({ mode: "passager", status: "participant", group: { groupId: nord }, pilote: { userId: "pilote" } });

    expect((await api.fetchPilotes(eventId)).map((p) => p.userId)).toEqual(["pilote"]);
    await expect(api.signUp(eventId, person("autre"), undefined, "passager")).rejects.toThrow();
    await expect(api.signUp(eventId, person("pilote"), undefined, "passager")).rejects.toThrow();
  });

  it("refuses a passager next to a Participant pilote while the Event is Complet", async () => {
    const api = createRegistrationsApi();
    await api.signUp(eventId, person("pilote"));
    await createEventsApi().changeStatus(eventId, "complet");
    await expect(api.signUp(eventId, person("passager"), undefined, "pilote")).rejects.toThrow();
  });

  it("counts passagers towards the Event and Groupe maximums", async () => {
    const api = createRegistrationsApi();
    await setMaximum(2);
    await api.signUp(eventId, person("pilote"), nord);
    await api.signUp(eventId, person("passager"), undefined, "pilote");
    expect((await api.signUp(eventId, person("autre"))).status).toBe("en_attente");

    await setMaximum(10);
    await api.setGroupMaximum(eventId, nord, 1);
    await expect(api.decideDemande(eventId, "pilote", CHEF, true)).rejects.toThrow(/maximum/);
  });

  it("moves the passager up, into and out of a Groupe with their pilote", async () => {
    const api = createRegistrationsApi();
    await setMaximum(1);
    await api.signUp(eventId, person("premier"));
    await api.signUp(eventId, person("pilote"));
    await api.signUp(eventId, person("passager"), undefined, "pilote");
    await expect(api.promote(eventId, "pilote")).rejects.toThrow();

    await setMaximum(3);
    await expect(api.promote(eventId, "passager")).rejects.toThrow();
    let roster = await api.promote(eventId, "pilote");
    expect(roster.participants.map((p) => p.personId)).toEqual(["premier", "pilote", "passager"]);

    roster = await api.placeInGroup(eventId, "pilote", nord);
    expect(roster.participants.find((p) => p.personId === "passager")?.group?.groupId).toBe(nord);
    await api.takeOutOfGroup(eventId, "pilote", CHEF);
    expect((await api.fetchRoster(eventId)).participants.find((p) => p.personId === "passager")?.group).toBeNull();
  });

  it("removes the passager with their pilote, but not the other way round", async () => {
    const api = createRegistrationsApi();
    await api.signUp(eventId, person("pilote"));
    await api.signUp(eventId, person("passager"), undefined, "pilote");
    await api.withdraw(eventId, "passager");
    expect((await api.fetchRoster(eventId)).participants.map((p) => p.personId)).toEqual(["pilote"]);

    await api.signUp(eventId, person("passager"), undefined, "pilote");
    await api.remove(eventId, "pilote");
    expect((await api.fetchRoster(eventId)).participants).toEqual([]);
  });
});
