import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchMyRegistrations, signUp, withdraw } from "./registrationsApi";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";

export function useMyRegistrations() {
  return useQuery({ queryKey: queryKeys.myRegistrations.all, queryFn: fetchMyRegistrations });
}

/** Inscription comme pilote et désinscription ; l'inscription utilise le téléphone du profil. */
export function useEventRegistration() {
  const queryClient = useQueryClient();
  const { user } = useAuth();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.myRegistrations.all });

  const register = useMutation({
    /** `phone` : numéro tout juste saisi, avant que le profil ne soit relu. */
    mutationFn: (input: { eventId: string; phone?: string }) => signUp(input.eventId, input.phone ?? user?.phone),
    onSuccess: invalidate,
  });

  const unregister = useMutation({
    mutationFn: withdraw,
    onSuccess: invalidate,
  });

  return { register, unregister };
}
