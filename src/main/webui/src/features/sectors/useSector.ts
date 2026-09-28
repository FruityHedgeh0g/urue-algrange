import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createSector, deleteSector, fetchSectorById, fetchSectors, SectorInput, updateSector } from "./sectorsApi";
import { queryKeys } from "../queryKeys";

export function useSectors() {
  return useQuery({ queryKey: queryKeys.sectors.all, queryFn: fetchSectors });
}

export function useSector(sectorId: string | undefined) {
  return useQuery({
    queryKey: queryKeys.sectors.detail(sectorId),
    queryFn: () => fetchSectorById(sectorId as string),
    enabled: Boolean(sectorId),
  });
}

/** Mutations sur les secteurs ; chacune rafraîchit la liste et les détails. */
export function useSectorMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.sectors.all });

  const update = useMutation({
    mutationFn: (input: { sectorId: string } & SectorInput) => updateSector(input.sectorId, input),
    onSuccess: invalidate,
  });

  const create = useMutation({
    mutationFn: createSector,
    onSuccess: invalidate,
  });

  const remove = useMutation({
    mutationFn: deleteSector,
    onSuccess: invalidate,
  });

  return { update, create, remove };
}
