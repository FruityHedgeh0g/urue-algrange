import { Configuration } from "./types";
import { apiFetch } from "../../lib/http";

/**
 * Les réglages du site, sur ConfigurationController : lus par tous (le site public affiche le titre et le
 * logo), changés par le seul Super admin, qui gère ce qui est commun à tous les Secteurs (ADR 0004).
 */
export const fetchConfigurations = () => apiFetch<Configuration[]>("/api/configurations");

export const updateConfiguration = (name: string, value: string) =>
  apiFetch<Configuration>(`/api/configurations/${encodeURIComponent(name)}`, { method: "PUT", body: JSON.stringify({ value }) });
