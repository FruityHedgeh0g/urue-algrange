import { mockMedias } from "./fixtures";
import { Media } from "./types";

/**
 * Client mocké, en attendant que /api/medias expose un contenu consultable
 * (aujourd'hui uniquement metadata). Signature alignée sur un futur fetch réel
 * pour un rebranchement sans changer les appelants (features/medias/useMedias).
 */
export async function fetchMedias(): Promise<Media[]> {
  return Promise.resolve(mockMedias);
}
