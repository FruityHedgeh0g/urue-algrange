import { Media } from "../medias/types";
import { mediaImage } from "../medias/mediaImage";

export const NAVBAR_LOGO_CONFIG_NAME = "navbar.logo";

const DEFAULT_NAVBAR_LOGO_ALT = "Une Rose Un Espoir - Algrange";

export function resolveNavbarLogo(mediaId: string, medias: Media[] | undefined) {
  return mediaImage(mediaId, medias, DEFAULT_NAVBAR_LOGO_ALT);
}
