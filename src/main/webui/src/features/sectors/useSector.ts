import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createSector, deleteSector, fetchSectorById, fetchSectors, updateSector } from "./sectorsApi";

const LIST_KEY = ["sectors"];

export function useSectors() {
  return useQuery({ queryKey: LIST_KEY, queryFn: fetchSectors });
}

export function useSector(sectorId: string | undefined) {
  return useQuery({
    queryKey: ["sectors", sectorId],
    queryFn: () => fetchSectorById(sectorId as string),
    enabled: Boolean(sectorId),
  });
}

export function useUpdateSector(sectorId: string | undefined) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (patch: { name: string; description: string }) => updateSector(sectorId as string, patch),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["sectors", sectorId] });
      queryClient.invalidateQueries({ queryKey: LIST_KEY });
    },
  });
}

export function useSectorMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: LIST_KEY });

  const update = useMutation({
    mutationFn: (input: { sectorId: string; name: string; description: string }) => updateSector(input.sectorId, input),
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
