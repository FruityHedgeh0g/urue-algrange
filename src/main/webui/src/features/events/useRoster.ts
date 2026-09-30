import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { decideDemande, fetchMonGroupe, fetchRoster, placeInGroup, promote, remove, setGroupMaximum, takeOutOfGroup } from "./registrationsApi";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";

/** Qui agit : un Chef n'agit que sur le Groupe qu'il mène, le Bureau sur tous. */
function useActor() {
  const { user, hasAtLeastRole } = useAuth();
  return { personId: user?.userId ?? "", bureau: hasAtLeastRole("bureau") };
}

/** Actions sur le Groupe d'une personne à un Événement, pour le Bureau et le Chef de groupe. */
export function useGroupActions() {
  const queryClient = useQueryClient();
  const actor = useActor();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const decide = useMutation({
    mutationFn: (input: { eventId: string; personId: string; accept: boolean }) =>
      decideDemande(input.eventId, input.personId, actor, input.accept),
    onSuccess: invalidate,
  });
  const takeOut = useMutation({
    mutationFn: (input: { eventId: string; personId: string }) => takeOutOfGroup(input.eventId, input.personId, actor),
    onSuccess: invalidate,
  });

  return { decide, takeOut };
}

/** Participants et Liste d'attente d'un Événement, gérés par le Bureau. */
export function useRoster(eventId: string) {
  const queryClient = useQueryClient();
  const roster = useQuery({ queryKey: queryKeys.myRegistrations.roster(eventId), queryFn: () => fetchRoster(eventId) });
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const moveUp = useMutation({ mutationFn: (personId: string) => promote(eventId, personId), onSuccess: invalidate });
  const removePerson = useMutation({ mutationFn: (personId: string) => remove(eventId, personId), onSuccess: invalidate });
  const place = useMutation({
    mutationFn: (input: { personId: string; groupId: string }) => placeInGroup(eventId, input.personId, input.groupId),
    onSuccess: invalidate,
  });
  const setMaximum = useMutation({
    mutationFn: (input: { groupId: string; maximum: number | null }) => setGroupMaximum(eventId, input.groupId, input.maximum),
    onSuccess: invalidate,
  });

  return { roster, moveUp, removePerson, place, setMaximum };
}

/** Mon groupe : le Groupe mené par la personne connectée et, par Événement, ses membres et Demandes. */
export function useMonGroupe() {
  const { user } = useAuth();
  return useQuery({
    queryKey: [...queryKeys.myRegistrations.all, "mon-groupe", user?.userId],
    queryFn: () => fetchMonGroupe(user?.userId ?? ""),
    enabled: Boolean(user),
  });
}
