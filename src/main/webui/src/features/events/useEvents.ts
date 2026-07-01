import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createEvent, deleteEvent, EventInput, fetchEventById, fetchEvents, updateEvent } from "./eventsApi";
import { EventOrganizer } from "./types";

export function useEvents() {
  return useQuery({ queryKey: ["events"], queryFn: fetchEvents });
}

export function useEvent(eventId: string | undefined) {
  return useQuery({
    queryKey: ["events", eventId],
    queryFn: () => fetchEventById(eventId as string),
    enabled: Boolean(eventId),
  });
}

export function useEventMutations() {
  const queryClient = useQueryClient();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: ["events"] });

  const update = useMutation({
    mutationFn: (input: { eventId: string } & EventInput) => updateEvent(input.eventId, input),
    onSuccess: invalidate,
  });

  const create = useMutation({
    mutationFn: (input: { data: EventInput; creator: EventOrganizer }) => createEvent(input.data, input.creator),
    onSuccess: invalidate,
  });

  const remove = useMutation({
    mutationFn: deleteEvent,
    onSuccess: invalidate,
  });

  return { update, create, remove };
}
