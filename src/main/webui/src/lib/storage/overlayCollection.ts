import { JsonStore } from "./jsonStore";

/**
 * Collection CRUD simulée : les fixtures servent de base, et les
 * modifications locales (éditions, créations, suppressions) sont superposées
 * depuis le stockage sous les clés `urue-<name>-overrides|created|deleted`.
 * Même forme qu'un futur client HTTP : list / get / create / update / remove.
 */
export interface Collection<T> {
  list(): Promise<T[]>;
  get(id: string): Promise<T | undefined>;
  create(item: T): Promise<void>;
  update(id: string, patch: Partial<T>): Promise<void>;
  remove(id: string): Promise<void>;
}

export interface OverlayCollectionOptions<T> {
  store: JsonStore;
  /** Segment des clés de stockage, ex : "event" → "urue-event-overrides". */
  name: string;
  fixtures: T[];
  idOf: (item: T) => string;
}

export function createOverlayCollection<T>({ store, name, fixtures, idOf }: OverlayCollectionOptions<T>): Collection<T> {
  const keys = {
    overrides: `urue-${name}-overrides`,
    created: `urue-${name}-created`,
    deleted: `urue-${name}-deleted`,
  };

  const readOverrides = () => store.read<Record<string, Partial<T>>>(keys.overrides, {});
  const readCreated = () => store.read<T[]>(keys.created, []);
  const readDeleted = () => store.read<string[]>(keys.deleted, []);

  const all = (): T[] => {
    const overrides = readOverrides();
    const deleted = readDeleted();
    return [...fixtures, ...readCreated()]
      .filter((item) => !deleted.includes(idOf(item)))
      .map((item) => ({ ...item, ...overrides[idOf(item)] }));
  };

  return {
    async list() {
      return all();
    },
    async get(id) {
      return all().find((item) => idOf(item) === id);
    },
    async create(item) {
      store.write(keys.created, [...readCreated(), item]);
    },
    async update(id, patch) {
      const overrides = readOverrides();
      overrides[id] = { ...overrides[id], ...patch };
      store.write(keys.overrides, overrides);
    },
    async remove(id) {
      const deleted = readDeleted();
      if (!deleted.includes(id)) store.write(keys.deleted, [...deleted, id]);
    },
  };
}
