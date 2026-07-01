import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  createCarouselItem,
  deleteCarouselItem,
  fetchActiveCarouselItems,
  fetchCarouselItems,
  moveCarouselItem,
  updateCarouselItem,
} from "./carouselApi";
import { CarouselItemInput } from "./types";

const QUERY_KEY = ["carousel-items"];

/** Tous les éléments (actifs et inactifs), pour l'écran d'administration. */
export function useCarouselItems() {
  return useQuery({ queryKey: QUERY_KEY, queryFn: fetchCarouselItems });
}

/** Éléments actifs uniquement, pour le carrousel public de l'accueil. */
export function useActiveCarouselItems() {
  return useQuery({ queryKey: [...QUERY_KEY, "active"], queryFn: fetchActiveCarouselItems });
}

export function useCarouselMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: QUERY_KEY });

  const create = useMutation({
    mutationFn: createCarouselItem,
    onSuccess: invalidate,
  });

  const update = useMutation({
    mutationFn: (input: { id: string } & CarouselItemInput) => updateCarouselItem(input.id, input),
    onSuccess: invalidate,
  });

  const remove = useMutation({
    mutationFn: deleteCarouselItem,
    onSuccess: invalidate,
  });

  const move = useMutation({
    mutationFn: (input: { id: string; direction: "up" | "down" }) => moveCarouselItem(input.id, input.direction),
    onSuccess: invalidate,
  });

  return { create, update, remove, move };
}
