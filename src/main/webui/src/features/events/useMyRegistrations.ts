import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchMyRegistrations, fetchPilotes, requestGroup, signUp, withdraw } from "./registrationsApi";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";

export function useMyRegistrations() {
  const { user } = useAuth();
  return useQuery({
    queryKey: [...queryKeys.myRegistrations.all, user?.userId],
    queryFn: fetchMyRegistrations,
    enabled: Boolean(user),
  });
}

/** Les pilotes inscrits à un Événement, parmi lesquels un passager choisit. */
export function usePilotes(eventId: string | undefined) {
  return useQuery({
    queryKey: queryKeys.myRegistrations.pilotes(eventId),
    queryFn: () => fetchPilotes(eventId ?? ""),
    enabled: Boolean(eventId),
  });
}

/** Inscription comme pilote ou passager, et désinscription de la personne connectée. */
export function useEventRegistration() {
  const queryClient = useQueryClient();
  const { user } = useAuth();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const register = useMutation({
    /** `piloteId` : s'inscrire comme passager de ce pilote (sans Demande de groupe). L'API lit le téléphone au profil. */
    mutationFn: (input: { eventId: string; phone?: string; groupId?: string; piloteId?: string }) => {
      if (!user) throw new Error("Connectez-vous pour vous inscrire.");
      return signUp(input.eventId, { groupId: input.groupId || undefined, piloteId: input.piloteId || undefined });
    },
    onSuccess: invalidate,
  });

  const unregister = useMutation({
    mutationFn: (eventId: string) => withdraw(eventId),
    onSuccess: invalidate,
  });

  /** Nouvelle Demande de groupe, par exemple après un refus. */
  const askGroup = useMutation({
    mutationFn: (input: { eventId: string; groupId: string }) => requestGroup(input.eventId, input.groupId),
    onSuccess: invalidate,
  });

  return { register, unregister, askGroup };
}
