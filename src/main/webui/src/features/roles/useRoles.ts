import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createRole, fetchRoles, RoleInput, updateRole } from "./rolesApi";
import { queryKeys } from "../queryKeys";

export function useRoles() {
  return useQuery({ queryKey: queryKeys.roles.all, queryFn: fetchRoles });
}

export function useRoleMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.roles.all });

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
