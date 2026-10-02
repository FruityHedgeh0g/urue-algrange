import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFeatureRequest, fetchFeatureRequests } from "./featureRequestsApi";
import { queryKeys } from "../queryKeys";

export function useFeatureRequests() {
  return useQuery({ queryKey: queryKeys.featureRequests.all, queryFn: fetchFeatureRequests });
}

export function useCreateFeatureRequest() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createFeatureRequest,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.featureRequests.all }),
  });
}
