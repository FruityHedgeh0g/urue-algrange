import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createRole, fetchRoles, RoleInput, updateRole } from "./rolesApi";

const QUERY_KEY = ["roles"];

export function useRoles() {
  return useQuery({ queryKey: QUERY_KEY, queryFn: fetchRoles });
}

export function useRoleMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: QUERY_KEY });

  const update = useMutation({
    mutationFn: (input: { roleId: string } & RoleInput) => updateRole(input.roleId, input),
    onSuccess: invalidate,
  });

  const create = useMutation({
    mutationFn: createRole,
    onSuccess: invalidate,
  });

  return { update, create };
}
