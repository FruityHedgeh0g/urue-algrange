import { Media } from "../medias/types";
import { assetUrl } from "../../lib/assetUrl";

export const NAVBAR_LOGO_CONFIG_NAME = "navbar.logo";

export const DEFAULT_NAVBAR_LOGO_SRC = assetUrl("logo_asso_transparent.png");
const DEFAULT_NAVBAR_LOGO_ALT = "Une Rose Un Espoir - Algrange";

export function resolveNavbarLogo(mediaId: string, medias: Media[] | undefined) {
  const media = mediaId ? medias?.find((m) => m.mediaId === mediaId) : undefined;
  return { src: media?.url ?? DEFAULT_NAVBAR_LOGO_SRC, alt: media?.alt ?? DEFAULT_NAVBAR_LOGO_ALT };
}
