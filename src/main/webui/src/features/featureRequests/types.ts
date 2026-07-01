/**
 * Suggestion d'évolution du site, remontée par le Bureau (cas d'usage
 * "Créer des demandes de feature"). Non modélisée côté backend actuellement.
 */
export interface FeatureRequest {
  id: string;
  title: string;
  description: string;
  createdAt: string;
  requestedBy: string;
}
