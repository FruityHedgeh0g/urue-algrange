import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { createSectorsApi } from "./sectorsApi";
import { createEventsApi } from "../events/eventsApi";
import { createGroupsApi } from "../groups/groupsApi";

const inHours = (hours: number) => new Date(Date.now() + hours * 3_600_000).toISOString().slice(0, 19);

let sectorId: string;
let groupId: string;
let upcoming: string;
let running: string;

describe("sectorsApi: who manages a Secteur, and the Secteur fermé", () => {
  beforeEach(async () => {
    const sectors = createSectorsApi();
    const groups = createGroupsApi();
    const events = createEventsApi();
    sectorId = (await sectors.createSector({ name: "Test Secteur", description: "Avant" })).sectorId;
    await groups.createGroup({ name: "Test Nord", description: "", area: "", sectorId });
    groupId = (await groups.fetchGroups()).find((g) => g.name === "Test Nord")!.groupId;
    await groups.setChef(groupId, { userId: "chef-nord", firstName: "Chef", lastName: "Nord" });

    const base = { description: "", sectorId };
    upcoming = (await events.createEvent({ ...base, name: "Test à venir", startDateTime: inHours(24), endDateTime: inHours(48) })).eventId;
    running = (await events.createEvent({ ...base, name: "Test en cours", startDateTime: inHours(-1), endDateTime: inHours(1) })).eventId;
    for (const id of [upcoming, running]) await events.changeStatus(id, "ouvert");
  });

  afterEach(() => localStorage.clear());

  it("lets only the Super admin rename; the Bureau keeps the description", async () => {
    const sectors = createSectorsApi();
    await expect(sectors.updateSector(sectorId, { name: "Test Renommé", description: "Avant" }, false)).rejects.toThrow();
    await sectors.updateSector(sectorId, { name: "Test Secteur", description: "Après" }, false);
    expect((await sectors.fetchSectorById(sectorId, false))?.description).toBe("Après");
    await sectors.updateSector(sectorId, { name: "Test Renommé", description: "Après" }, true);
    expect((await sectors.fetchSectorById(sectorId, false))?.name).toBe("Test Renommé");
  });

  it("closes a Secteur: Affectations end, unfinished Events are Annulé or Archivé, all hidden below the Super admin", async () => {
    const sectors = createSectorsApi();
    await sectors.closeSector(sectorId);

    expect((await sectors.fetchSectors(false)).map((s) => s.sectorId)).not.toContain(sectorId);
    expect(await sectors.fetchSectorById(sectorId, false)).toBeUndefined();
    expect((await sectors.fetchSectorById(sectorId, true))?.closed).toBe(true);

    const groups = createGroupsApi();
    expect((await groups.fetchVisibleGroups(false)).map((g) => g.groupId)).not.toContain(groupId);
    expect((await groups.fetchVisibleGroups(true)).find((g) => g.groupId === groupId)?.chef).toBeNull();

    const events = createEventsApi();
    expect((await events.fetchEvents(true, false)).map((e) => e.eventId)).not.toContain(upcoming);
    expect(await events.fetchEventById(upcoming, true, false)).toBeUndefined();
    expect((await events.fetchEventById(upcoming, true, true))?.status).toBe("annule");
    expect((await events.fetchEventById(running, true, true))?.status).toBe("archive");
  });

  it("keeps a Secteur fermé read-only until the Super admin reopens it", async () => {
    const sectors = createSectorsApi();
    await sectors.closeSector(sectorId);
    await expect(sectors.updateSector(sectorId, { name: "Test Secteur", description: "Après" }, true)).rejects.toThrow();

    await sectors.reopenSector(sectorId);
    expect((await sectors.fetchSectorById(sectorId, false))?.closed).toBe(false);
    expect((await createEventsApi().fetchEventById(upcoming, true, false))?.status).toBe("annule");
    expect((await createGroupsApi().fetchVisibleGroups(false)).find((g) => g.groupId === groupId)?.chef).toBeNull();
  });
});
