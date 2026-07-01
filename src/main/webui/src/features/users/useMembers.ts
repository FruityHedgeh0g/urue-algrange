import { useQuery } from "@tanstack/react-query";
import { fetchMembersByGroupIds } from "./usersApi";

export function useMembersByGroupIds(groupIds: string[]) {
  return useQuery({
    queryKey: ["members", ...groupIds],
    queryFn: () => fetchMembersByGroupIds(groupIds),
    enabled: groupIds.length > 0,
  });
}
