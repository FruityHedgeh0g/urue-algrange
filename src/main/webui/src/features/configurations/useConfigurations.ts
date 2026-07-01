import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchConfigurations, updateConfiguration } from "./configurationsApi";

const QUERY_KEY = ["configurations"];

export function useConfigurations() {
  return useQuery({ queryKey: QUERY_KEY, queryFn: fetchConfigurations });
}

export function useUpdateConfiguration() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (input: { name: string; value: string }) => updateConfiguration(input.name, input.value),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUERY_KEY }),
  });
}
