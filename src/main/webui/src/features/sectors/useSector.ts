import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { closeSector, createSector, fetchSectorById, fetchSectors, reopenSector, SectorInput, updateSector } from "./sectorsApi";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";

/** Seul le Super admin voit un Secteur fermé et ce qu'il contient, et renomme un Secteur. */
export function useIsSuperAdmin() {
  return useAuth().hasAtLeastRole("super_admin");
}

export function useSectors() {
  const seesClosed = useIsSuperAdmin();
  return useQuery({ queryKey: [...queryKeys.sectors.all, { seesClosed }], queryFn: () => fetchSectors(seesClosed) });
}

export function useSector(sectorId: string | undefined) {
  const seesClosed = useIsSuperAdmin();
  return useQuery({
    queryKey: [...queryKeys.sectors.detail(sectorId), { seesClosed }],
    queryFn: () => fetchSectorById(sectorId as string, seesClosed),
    enabled: Boolean(sectorId),
  });
}

/** Mutations sur les secteurs ; fermer et rouvrir rafraîchissent aussi Groupes et Événements. */
export function useSectorMutations() {
  const queryClient = useQueryClient();
  const mayRename = useIsSuperAdmin();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.sectors.all });
  const invalidateAll = () =>
    Promise.all([
      invalidate(),
      queryClient.invalidateQueries({ queryKey: queryKeys.groups.all }),
      queryClient.invalidateQueries({ queryKey: queryKeys.events.all }),
    ]);

  const update = useMutation({
    mutationFn: (input: { sectorId: string } & SectorInput) =>
      updateSector(input.sectorId, { name: input.name, description: input.description }, mayRename),
    onSuccess: invalidate,
  });
  const create = useMutation({ mutationFn: createSector, onSuccess: invalidate });
  const close = useMutation({ mutationFn: closeSector, onSuccess: invalidateAll });
  const reopen = useMutation({ mutationFn: reopenSector, onSuccess: invalidateAll });

  return { update, create, close, reopen };
}
