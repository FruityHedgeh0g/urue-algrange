import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchAllMembers, fetchMembersByGroupIds, MemberInput, updateMember } from "./usersApi";
import { queryKeys } from "../queryKeys";

export function useMembersByGroupIds(groupIds: string[]) {
  return useQuery({
    queryKey: queryKeys.members.byGroups(groupIds),
    queryFn: () => fetchMembersByGroupIds(groupIds),
    enabled: groupIds.length > 0,
  });
}

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
