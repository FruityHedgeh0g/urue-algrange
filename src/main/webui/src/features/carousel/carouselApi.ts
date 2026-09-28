import { mockCarouselItems } from "./fixtures";
import { CarouselItem, CarouselItemInput } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";

const STORAGE_KEY = "urue-carousel-items";

function sorted(items: CarouselItem[]): CarouselItem[] {
  return [...items].sort((a, b) => a.order - b.order);
}

/**
 * Client mocké — aucun endpoint /api/carousel n'existe côté backend.
 * Toute la liste (ordre inclus) est persistée en un seul bloc, ce qui
 * simplifie la réorganisation par rapport à la collection superposée
 * (lib/storage/overlayCollection) utilisée pour les autres listes.
 */
export function createCarouselApi(store: JsonStore = localJsonStore) {
  const readItems = () => store.read<CarouselItem[]>(STORAGE_KEY, mockCarouselItems);
  const writeItems = (items: CarouselItem[]) => store.write(STORAGE_KEY, items);

  return {
    fetchCarouselItems: async () => sorted(readItems()),
    fetchActiveCarouselItems: async () => sorted(readItems().filter((item) => item.active)),
    createCarouselItem: async (input: CarouselItemInput): Promise<void> => {
      const items = readItems();
      const nextOrder = items.reduce((max, item) => Math.max(max, item.order), 0) + 1;
      writeItems([...items, { id: `carousel-${Date.now()}`, order: nextOrder, ...input }]);
    },
    updateCarouselItem: async (id: string, patch: CarouselItemInput): Promise<void> => {
      writeItems(readItems().map((item) => (item.id === id ? { ...item, ...patch } : item)));
    },
    deleteCarouselItem: async (id: string): Promise<void> => {
      writeItems(readItems().filter((item) => item.id !== id));
    },
    moveCarouselItem: async (id: string, direction: "up" | "down"): Promise<void> => {
      const items = sorted(readItems());
      const index = items.findIndex((item) => item.id === id);
      const swapWith = direction === "up" ? index - 1 : index + 1;
      if (index === -1 || swapWith < 0 || swapWith >= items.length) return;
      const a = items[index];
      const b = items[swapWith];
      items[index] = { ...a, order: b.order };
      items[swapWith] = { ...b, order: a.order };
      writeItems(items);
    },
  };
}

export const {
  fetchCarouselItems,
  fetchActiveCarouselItems,
  createCarouselItem,
  updateCarouselItem,
  deleteCarouselItem,
  moveCarouselItem,
} = createCarouselApi();
