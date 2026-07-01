import { useQuery } from "@tanstack/react-query";
import { fetchEventById, fetchEvents } from "./eventsApi";

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
