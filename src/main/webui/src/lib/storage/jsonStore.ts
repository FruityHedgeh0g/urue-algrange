/**
 * Stockage clé/valeur JSON utilisé par les clients mockés (features/*Api.ts).
 * Deux adaptateurs : localStorage pour l'application, mémoire pour les tests.
 */
export interface JsonStore {
  read<T>(key: string, fallback: T): T;
  write(key: string, value: unknown): void;
}

export const localJsonStore: JsonStore = {
  read<T>(key: string, fallback: T): T {
    try {
      const saved = localStorage.getItem(key);
      return saved ? (JSON.parse(saved) as T) : fallback;
    } catch {
      return fallback;
    }
  },
  write(key, value) {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch {
      // stockage indisponible : la modification reste active pour la session
    }
  },
};

export function createMemoryJsonStore(initial: Record<string, unknown> = {}): JsonStore {
  const data = new Map<string, string>(Object.entries(initial).map(([k, v]) => [k, JSON.stringify(v)]));
  return {
    read<T>(key: string, fallback: T): T {
      const saved = data.get(key);
      return saved ? (JSON.parse(saved) as T) : fallback;
    },
    write(key, value) {
      data.set(key, JSON.stringify(value));
    },
  };
}
