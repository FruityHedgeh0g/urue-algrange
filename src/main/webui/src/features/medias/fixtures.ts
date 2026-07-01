import { placeholderImage } from "../../lib/placeholderImage";
import { Media } from "./types";

const captions = [
  "Collecte 2025 - Départ du convoi",
  "Remise de dons à l'hôpital",
  "Balade solidaire d'Algrange",
  "Stand de sensibilisation",
  "Les bénévoles sur le village",
  "Arrivée place de la mairie",
];

export const mockMedias: Media[] = captions.map((caption, i) => {
  const mediaId = `media-${i + 1}`;
  return {
    mediaId,
    fileKey: `photos/${mediaId}.jpg`,
    originalFilename: `${mediaId}.jpg`,
    contentType: "image/jpeg",
    fileSize: 245_000,
    url: placeholderImage(mediaId, caption),
    alt: caption,
  };
});
