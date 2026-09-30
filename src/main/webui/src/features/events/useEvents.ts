import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { changeStatus, createEvent, EventInput, fetchEventById, fetchEvents, updateEvent } from "./eventsApi";
import { EventStatus } from "./status";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";
import { useIsSuperAdmin } from "../sectors/useSector";

/** Seuls le Bureau et au-dessus voient les Événements en Planification. */
function useSeesPlanification() {
  return useAuth().hasAtLeastRole("bureau");
}

export function useEvents() {
  const seesPlanification = useSeesPlanification();
  const seesClosed = useIsSuperAdmin();
  return useQuery({
    queryKey: [...queryKeys.events.all, { seesPlanification, seesClosed }],
    queryFn: () => fetchEvents(seesPlanification, seesClosed),
  });
}

export function useEvent(eventId: string | undefined) {
  const seesPlanification = useSeesPlanification();
  const seesClosed = useIsSuperAdmin();
  return useQuery({
    queryKey: [...queryKeys.events.detail(eventId), { seesPlanification, seesClosed }],
    queryFn: () => fetchEventById(eventId as string, seesPlanification, seesClosed),
    enabled: Boolean(eventId),
  });
}

export function useEventMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.events.all });

  const update = useMutation({
    mutationFn: ({ eventId, ...patch }: { eventId: string } & Omit<EventInput, "sectorId">) => updateEvent(eventId, patch),
    onSuccess: invalidate,
  });

  const create = useMutation({ mutationFn: createEvent, onSuccess: invalidate });

  const moveTo = useMutation({
    mutationFn: (input: { eventId: string; status: EventStatus }) => changeStatus(input.eventId, input.status),
    onSuccess: invalidate,
  });

  return { update, create, moveTo };
}
