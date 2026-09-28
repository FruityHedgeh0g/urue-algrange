import { describe, expect, it } from "vitest";
import { createMemoryJsonStore } from "./jsonStore";
import { createOverlayCollection } from "./overlayCollection";

interface Item {
  id: string;
  name: string;
}

const fixtures: Item[] = [
  { id: "a", name: "Alpha" },
  { id: "b", name: "Bravo" },
];

const makeCollection = (store = createMemoryJsonStore()) =>
  createOverlayCollection<Item>({ store, name: "item", fixtures, idOf: (i) => i.id });

describe("createOverlayCollection", () => {
  it("lists the fixtures when nothing was changed", async () => {
    expect(await makeCollection().list()).toEqual(fixtures);
  });

  it("applies updates on top of a fixture", async () => {
    const items = makeCollection();
    await items.update("a", { name: "Alpha bis" });
    expect(await items.get("a")).toEqual({ id: "a", name: "Alpha bis" });
  });

  it("appends created items and applies later updates to them", async () => {
    const items = makeCollection();
    await items.create({ id: "c", name: "Charlie" });
    await items.update("c", { name: "Charlie bis" });
    expect(await items.list()).toEqual([...fixtures, { id: "c", name: "Charlie bis" }]);
  });

  it("hides removed fixtures and created items", async () => {
    const items = makeCollection();
    await items.create({ id: "c", name: "Charlie" });
    await items.remove("a");
    await items.remove("c");
    expect(await items.list()).toEqual([{ id: "b", name: "Bravo" }]);
    expect(await items.get("a")).toBeUndefined();
  });

  it("persists changes under the urue-<name>-* keys of its store", async () => {
    const store = createMemoryJsonStore();
    await makeCollection(store).update("b", { name: "Bravo bis" });
    expect(store.read("urue-item-overrides", {})).toEqual({ b: { name: "Bravo bis" } });
    expect(await makeCollection(store).get("b")).toEqual({ id: "b", name: "Bravo bis" });
  });
});
