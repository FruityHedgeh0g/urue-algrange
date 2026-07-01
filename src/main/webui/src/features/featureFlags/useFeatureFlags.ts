import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchFeatureFlags, setFeatureFlagActive } from "./featureFlagsApi";

const QUERY_KEY = ["feature-flags"];

export function useFeatureFlags() {
  return useQuery({ queryKey: QUERY_KEY, queryFn: fetchFeatureFlags });
}

export function useSetFeatureFlagActive() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { name: string; isActive: boolean }) => setFeatureFlagActive(input.name, input.isActive),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUERY_KEY }),
  });
}
