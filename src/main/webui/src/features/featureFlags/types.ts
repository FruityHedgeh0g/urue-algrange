/** Noms des fonctionnalités activables, lus par useFeature et la carte d'accès (auth/access.ts). */
export type FeatureName = "dons-en-ligne" | "inscription-evenements" | "galerie-photos";

/** Reflète FeatureDto côté backend. */
export interface FeatureFlag {
  name: FeatureName;
  description: string;
  isActive: boolean;
}
