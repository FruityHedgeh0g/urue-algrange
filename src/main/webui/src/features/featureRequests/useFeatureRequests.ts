import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFeatureRequest, fetchFeatureRequests } from "./featureRequestsApi";

const QUERY_KEY = ["feature-requests"];

export function useFeatureRequests() {
  return useQuery({ queryKey: QUERY_KEY, queryFn: fetchFeatureRequests });
}

export function useCreateFeatureRequest() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createFeatureRequest,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUERY_KEY }),
  });
}
