import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchConfigurations, updateConfiguration } from "./configurationsApi";
import { queryKeys } from "../queryKeys";

export function useConfigurations() {
  return useQuery({ queryKey: queryKeys.configurations.all, queryFn: fetchConfigurations });
}

export function useUpdateConfiguration() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { name: string; value: string }) => updateConfiguration(input.name, input.value),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.configurations.all }),
  });
}
