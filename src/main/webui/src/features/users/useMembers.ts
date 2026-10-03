import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { appointPresident, changeRole, fetchAllMembers, MemberInput, updateMember } from "./usersApi";
import { RoleId } from "../../auth/roles";
import { queryKeys } from "../queryKeys";

export function useAllMembers() {
  return useQuery({ queryKey: queryKeys.members.list, queryFn: fetchAllMembers });
}

export function useUpdateMember() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { userId: string } & MemberInput) => updateMember(input.userId, input),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.members.all }),
  });
}

export function useChangeRole() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { userId: string; role: RoleId; sectorId?: string }) => changeRole(input.userId, input.role, input.sectorId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.members.all }),
  });
}

export function useAppointPresident() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (userId: string) => appointPresident(userId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.members.all }),
  });
}
