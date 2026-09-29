import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchRoster, promote, remove } from "./registrationsApi";
import { queryKeys } from "../queryKeys";

/** Participants et Liste d'attente d'un Événement, gérés par le Bureau. */
export function useRoster(eventId: string) {
  const queryClient = useQueryClient();
  const roster = useQuery({ queryKey: queryKeys.myRegistrations.roster(eventId), queryFn: () => fetchRoster(eventId) });
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const moveUp = useMutation({ mutationFn: (personId: string) => promote(eventId, personId), onSuccess: invalidate });
  const removePerson = useMutation({ mutationFn: (personId: string) => remove(eventId, personId), onSuccess: invalidate });

  return { roster, moveUp, removePerson };
}
