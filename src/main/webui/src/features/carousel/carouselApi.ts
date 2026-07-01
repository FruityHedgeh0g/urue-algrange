import { mockCarouselItems } from "./fixtures";
import { CarouselItem, CarouselItemInput } from "./types";

const STORAGE_KEY = "urue-carousel-items";

/**
 * Client mocké — aucun endpoint /api/carousel n'existe côté backend.
 * Toute la liste (ordre inclus) est persistée en un seul bloc en
 * localStorage, ce qui simplifie la réorganisation par rapport au motif
 * overrides/created utilisé pour les listes plus volumineuses.
 */
function readItems(): CarouselItem[] {
  try {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved ? JSON.parse(saved) : mockCarouselItems;
  } catch {
    return mockCarouselItems;
  }
}

function writeItems(items: CarouselItem[]) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
  } catch {
    // stockage indisponible : la modification reste active pour la session
  }
}

function sorted(items: CarouselItem[]): CarouselItem[] {
  return [...items].sort((a, b) => a.order - b.order);
}

export async function fetchCarouselItems(): Promise<CarouselItem[]> {
  return sorted(readItems());
}

export async function fetchActiveCarouselItems(): Promise<CarouselItem[]> {
  return sorted(readItems().filter((item) => item.active));
}

export async function createCarouselItem(input: CarouselItemInput): Promise<void> {
  const items = readItems();
  const nextOrder = items.reduce((max, item) => Math.max(max, item.order), 0) + 1;
  items.push({ id: `carousel-${Date.now()}`, order: nextOrder, ...input });
  writeItems(items);
  return Promise.resolve();
}

export async function updateCarouselItem(id: string, patch: CarouselItemInput): Promise<void> {
  const items = readItems().map((item) => (item.id === id ? { ...item, ...patch } : item));
  writeItems(items);
  return Promise.resolve();
}

export async function deleteCarouselItem(id: string): Promise<void> {
  writeItems(readItems().filter((item) => item.id !== id));
  return Promise.resolve();
}

export async function moveCarouselItem(id: string, direction: "up" | "down"): Promise<void> {
  const items = sorted(readItems());
  const index = items.findIndex((item) => item.id === id);
  const swapWith = direction === "up" ? index - 1 : index + 1;
  if (index === -1 || swapWith < 0 || swapWith >= items.length) return Promise.resolve();

  const orderA = items[index].order;
  const orderB = items[swapWith].order;
  items[index].order = orderB;
  items[swapWith].order = orderA;
  writeItems(items);
  return Promise.resolve();
}
