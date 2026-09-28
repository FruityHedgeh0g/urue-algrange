import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchFeatureFlags, setFeatureFlagActive } from "./featureFlagsApi";
import { FeatureName } from "./types";
import { queryKeys } from "../queryKeys";

export function useFeatureFlags() {
  return useQuery({ queryKey: queryKeys.featureFlags.all, queryFn: fetchFeatureFlags });
}

/** État des fonctionnalités ; `isActive` renvoie false tant que les flags ne sont pas chargés. */
export function useFeatures() {
  const { data, isSuccess } = useFeatureFlags();
  return {
    ready: isSuccess,
    isActive: (name: FeatureName) => data?.find((f) => f.name === name)?.isActive ?? false,
  };
}

export function useFeature(name: FeatureName): boolean {
  return useFeatures().isActive(name);
}

export function useSetFeatureFlagActive() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { name: FeatureName; isActive: boolean }) => setFeatureFlagActive(input.name, input.isActive),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.featureFlags.all }),
  });
}
