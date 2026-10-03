/** Suggestion d'évolution du site, remontée par le Bureau : reflète FeatureRequestDto. `requestedBy` : le nom de son auteur. */
export interface FeatureRequest {
  id: string;
  title: string;
  description: string;
  createdAt: string;
  requestedBy: string;
}
