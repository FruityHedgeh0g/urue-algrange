import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchFeatureFlags, setFeatureFlagActive } from "./featureFlagsApi";
import { FeatureName } from "./types";
import { queryKeys } from "../queryKeys";

export function useFeatureFlags() {
  return useQuery({ queryKey: queryKeys.featureFlags.all, queryFn: fetchFeatureFlags });
}

export function useSetFeatureFlagActive() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { name: FeatureName; isActive: boolean }) => setFeatureFlagActive(input.name, input.isActive),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.featureFlags.all }),
  });
}
