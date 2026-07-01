import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchSectorById, updateSector } from "./sectorsApi";

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
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["sectors", sectorId] }),
  });
}
