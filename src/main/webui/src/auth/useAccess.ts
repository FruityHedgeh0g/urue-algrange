import { useAuth } from "./AuthContext";
import { AccessContext, AccessId, AccessSection, canAccess, mainNav, navFor } from "./access";
import { useFeatures } from "../features/featureFlags/useFeatureFlags";

/** Carte d'accès appliquée à l'utilisateur courant (rôle + fonctionnalités actives). */
export function useAccess() {
  const { role } = useAuth();
  const features = useFeatures();
  const ctx: AccessContext = { role, isFeatureActive: features.isActive };
  return {
    /** false tant que l'état des fonctionnalités n'est pas chargé. */
    ready: features.ready,
    canAccess: (id: AccessId) => canAccess(id, ctx),
    navFor: (section: AccessSection) => navFor(section, ctx),
    mainNav: () => mainNav(ctx),
  };
}
