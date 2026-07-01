/**
 * Reflète MediaDto/NestedMediaDto côté backend, complété d'une `url`
 * d'affichage : le backend n'expose pas encore d'endpoint de contenu pour les
 * médias, cette URL est donc dérivée côté mock en attendant.
 */
export interface Media {
  mediaId: string;
  fileKey: string;
  originalFilename: string;
  contentType: string;
  fileSize: number;
  url: string;
  alt: string;
}
