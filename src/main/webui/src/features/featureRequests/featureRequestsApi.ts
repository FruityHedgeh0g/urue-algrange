import { mockFeatureRequests } from "./fixtures";
import { FeatureRequest } from "./types";

const CREATED_KEY = "urue-feature-requests-created";

function readCreated(): FeatureRequest[] {
  try {
    const saved = localStorage.getItem(CREATED_KEY);
    return saved ? JSON.parse(saved) : [];
  } catch {
    return [];
  }
}

function writeCreated(items: FeatureRequest[]) {
  try {
    localStorage.setItem(CREATED_KEY, JSON.stringify(items));
  } catch {
    // stockage indisponible : la demande reste active pour la session
  }
}

export async function fetchFeatureRequests(): Promise<FeatureRequest[]> {
  const created = readCreated();
  return Promise.resolve([...created, ...mockFeatureRequests]);
}

export async function createFeatureRequest(input: { title: string; description: string; requestedBy: string }): Promise<void> {
  const created = readCreated();
  created.unshift({ id: `fr-${Date.now()}`, createdAt: new Date().toISOString(), ...input });
  writeCreated(created);
  return Promise.resolve();
}
