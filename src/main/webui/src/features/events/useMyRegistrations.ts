import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchMyEventIds, registerForEvent, unregisterFromEvent } from "./registrationsApi";
import { queryKeys } from "../queryKeys";

export function useMyEventIds() {
  return useQuery({ queryKey: queryKeys.myRegistrations.all, queryFn: fetchMyEventIds });
}

export function useEventRegistration() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const register = useMutation({
    mutationFn: registerForEvent,
    onSuccess: invalidate,
  });

  const unregister = useMutation({
    mutationFn: unregisterFromEvent,
    onSuccess: invalidate,
  });

  return { register, unregister };
}
