import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchMyEventIds, registerForEvent, unregisterFromEvent } from "./registrationsApi";

const QUERY_KEY = ["my-event-registrations"];

export function useMyEventIds() {
  return useQuery({ queryKey: QUERY_KEY, queryFn: fetchMyEventIds });
}

export function useEventRegistration() {
  const queryClient = useQueryClient();

  const register = useMutation({
    mutationFn: registerForEvent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUERY_KEY }),
  });

  const unregister = useMutation({
    mutationFn: unregisterFromEvent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUERY_KEY }),
  });

  return { register, unregister };
}
