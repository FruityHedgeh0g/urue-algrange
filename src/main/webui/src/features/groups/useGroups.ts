import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { clearChef, createGroup, fetchGroups, GroupInput, setChef, updateGroup } from "./groupsApi";
import { GroupChef } from "./types";
import { queryKeys } from "../queryKeys";
import { useIsSuperAdmin } from "../sectors/useSector";

/** Les Groupes visibles : l'API ne montre ceux d'un Secteur fermé qu'au Super admin. */
export function useGroups() {
  const seesClosed = useIsSuperAdmin();
  return useQuery({ queryKey: [...queryKeys.groups.all, { seesClosed }], queryFn: fetchGroups });
}

/** Mutations sur les Groupes ; chacune rafraîchit la liste. */
export function useGroupMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.groups.all });

  const create = useMutation({ mutationFn: createGroup, onSuccess: invalidate });
  const update = useMutation({
    mutationFn: (input: { groupId: string } & GroupInput) => updateGroup(input.groupId, input),
    onSuccess: invalidate,
  });
  /** Affectation : donne le Groupe à un Chef, ou le laisse sans chef (null). */
  const affectation = useMutation({
    mutationFn: (input: { groupId: string; chef: GroupChef | null }) =>
      input.chef ? setChef(input.groupId, input.chef) : clearChef(input.groupId),
    onSuccess: invalidate,
  });

  return { create, update, affectation };
}
