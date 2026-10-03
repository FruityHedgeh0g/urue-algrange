import { FeatureFlag, FeatureName } from "./types";
import { apiFetch } from "../../lib/http";

/**
 * Les Fonctionnalités, sur FeatureController : lues par tous (le site public s'y conforme), activées et
 * désactivées par le seul Super admin (#27).
 */
export const fetchFeatureFlags = () => apiFetch<FeatureFlag[]>("/api/features");

export const setFeatureFlagActive = (name: FeatureName, isActive: boolean) =>
  apiFetch<FeatureFlag>(`/api/features/${encodeURIComponent(name)}`, { method: "PUT", body: JSON.stringify({ isActive }) });
