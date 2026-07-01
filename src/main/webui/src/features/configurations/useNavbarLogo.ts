import { useConfigurations } from "./useConfigurations";
import { useMedias } from "../medias/useMedias";
import { NAVBAR_LOGO_CONFIG_NAME, resolveNavbarLogo } from "./navbarLogo";

export function useNavbarLogo() {
  const { data: configurations } = useConfigurations();
  const { data: medias } = useMedias();
  const mediaId = configurations?.find((c) => c.name === NAVBAR_LOGO_CONFIG_NAME)?.value ?? "";
  return resolveNavbarLogo(mediaId, medias);
}
