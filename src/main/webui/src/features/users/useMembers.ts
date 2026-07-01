import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchAllMembers, fetchMembersByGroupIds, updateMember } from "./usersApi";

export function useMembersByGroupIds(groupIds: string[]) {
  return useQuery({
    queryKey: ["members", ...groupIds],
    queryFn: () => fetchMembersByGroupIds(groupIds),
    enabled: groupIds.length > 0,
  });
}

export function useAllMembers() {
  return useQuery({ queryKey: ["members", "all"], queryFn: fetchAllMembers });
}

export function useUpdateMember() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { userId: string; firstName: string; lastName: string; groupId: string }) => updateMember(input.userId, input),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["members"] }),
  });
}
