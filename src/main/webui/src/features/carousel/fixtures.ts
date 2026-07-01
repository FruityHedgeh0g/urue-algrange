import { CarouselItem } from "./types";

export const mockCarouselItems: CarouselItem[] = [
  {
    id: "carousel-1",
    title: "Bienvenue à Une Rose Un Espoir - Algrange",
    caption: "Ensemble contre le cancer",
    mediaId: null,
    linkTo: null,
    active: true,
    order: 1,
  },
  {
    id: "carousel-2",
    title: "Collecte 2025",
    caption: "Rejoignez l'aventure et devenez bénévole.",
    mediaId: "media-3",
    linkTo: "/#benevolat",
    active: true,
    order: 2,
  },
  {
    id: "carousel-3",
    title: "Actualités & Événements",
    caption: "Suivez nos dernières publications et actions.",
    mediaId: "media-1",
    linkTo: "/evenements",
    active: true,
    order: 3,
  },
];
