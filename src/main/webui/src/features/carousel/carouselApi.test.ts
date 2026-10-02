import { describe, expect, it } from "vitest";
import { createMemoryJsonStore } from "../../lib/storage/jsonStore";
import { createCarouselApi } from "./carouselApi";
import { mockCarouselItems } from "./fixtures";

const ids = (items: { id: string }[]) => items.map((i) => i.id);

describe("carousel api", () => {
  it("moves an item up and down without touching the fixtures", async () => {
    const api = createCarouselApi(createMemoryJsonStore());
    const fixtureOrders = mockCarouselItems.map((i) => i.order);

    await api.moveCarouselItem("carousel-2", "up");
    expect(ids(await api.fetchCarouselItems()).slice(0, 3)).toEqual(["carousel-2", "carousel-1", "carousel-3"]);

    await api.moveCarouselItem("carousel-2", "down");
    expect(ids(await api.fetchCarouselItems()).slice(0, 3)).toEqual(["carousel-1", "carousel-2", "carousel-3"]);
    expect(mockCarouselItems.map((i) => i.order)).toEqual(fixtureOrders);
  });

  it("ignores moves past either end", async () => {
    const api = createCarouselApi(createMemoryJsonStore());
    const before = ids(await api.fetchCarouselItems());
    await api.moveCarouselItem(before[0], "up");
    await api.moveCarouselItem(before[before.length - 1], "down");
    expect(ids(await api.fetchCarouselItems())).toEqual(before);
  });

  it("appends created items at the end and hides inactive ones from the public list", async () => {
    const api = createCarouselApi(createMemoryJsonStore());
    await api.createCarouselItem({ title: "Nouveau", caption: "", mediaId: null, linkTo: null, active: false });
    const all = await api.fetchCarouselItems();
    expect(all[all.length - 1].title).toBe("Nouveau");
    expect((await api.fetchActiveCarouselItems()).some((i) => i.title === "Nouveau")).toBe(false);
  });
});
