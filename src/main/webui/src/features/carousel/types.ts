/**
 * Élément du carrousel d'accueil. Aucune contrepartie backend actuelle (pas
 * de contrôleur REST) : entièrement mocké en attendant, avec une forme
 * pensée pour se rapprocher d'un futur CarouselItemDto.
 */
export interface CarouselItem {
  id: string;
  title: string;
  caption: string;
  /** Référence vers features/medias ; vide = logo de l'association (image par défaut). */
  mediaId: string | null;
  /** Route interne optionnelle (ex : "/evenements" ou "/#benevolat"). */
  linkTo: string | null;
  /** Permet de désactiver temporairement un slide sans le supprimer. */
  active: boolean;
  order: number;
}

export type CarouselItemInput = Omit<CarouselItem, "id" | "order">;
