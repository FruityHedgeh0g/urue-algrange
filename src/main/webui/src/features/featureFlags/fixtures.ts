import { FeatureFlag } from "./types";

export const mockFeatureFlags: FeatureFlag[] = [
  { name: "dons-en-ligne", description: "Afficher le module de don en ligne sur le site public.", isActive: false },
  { name: "inscription-evenements", description: "Permettre l'inscription en ligne aux événements.", isActive: true },
  { name: "galerie-photos", description: "Afficher la galerie photos publique.", isActive: true },
];
