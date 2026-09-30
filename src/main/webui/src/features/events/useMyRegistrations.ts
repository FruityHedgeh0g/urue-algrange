import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchMyRegistrations, requestGroup, signUp, withdraw } from "./registrationsApi";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";

export function useMyRegistrations() {
  const { user } = useAuth();
  return useQuery({
    queryKey: [...queryKeys.myRegistrations.all, user?.userId],
    queryFn: () => fetchMyRegistrations(user?.userId ?? ""),
    enabled: Boolean(user),
  });
}

/** Inscription comme pilote et désinscription de la personne connectée. */
export function useEventRegistration() {
  const queryClient = useQueryClient();
  const { user } = useAuth();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const register = useMutation({
    /** `phone` : numéro tout juste saisi, avant que le profil ne soit relu. */
    mutationFn: (input: { eventId: string; phone?: string; groupId?: string }) => {
      if (!user) throw new Error("Connectez-vous pour vous inscrire.");
      return signUp(input.eventId, { ...user, phone: input.phone ?? user.phone }, input.groupId || undefined);
    },
    onSuccess: invalidate,
  });

  const unregister = useMutation({
    mutationFn: (eventId: string) => withdraw(eventId, user?.userId ?? ""),
    onSuccess: invalidate,
  });

  /** Nouvelle Demande de groupe, par exemple après un refus. */
  const askGroup = useMutation({
    mutationFn: (input: { eventId: string; groupId: string }) => requestGroup(input.eventId, user?.userId ?? "", input.groupId),
    onSuccess: invalidate,
  });

  return { register, unregister, askGroup };
}
