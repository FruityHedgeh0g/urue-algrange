import { FeatureRequest } from "./types";
import { apiFetch } from "../../lib/http";

/** Les demandes de fonctionnalités, sur FeatureRequestController : écrites et lues par le Bureau ; l'API en signe l'auteur. */
export const fetchFeatureRequests = () => apiFetch<FeatureRequest[]>("/api/feature-requests");

export const createFeatureRequest = (input: { title: string; description: string }) =>
  apiFetch<FeatureRequest>("/api/feature-requests", { method: "POST", body: JSON.stringify(input) });
