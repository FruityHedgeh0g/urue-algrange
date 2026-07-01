import React from "react";
import { useEvents } from "../../../features/events/useEvents";
import { isUpcoming } from "../../../features/events/eventsApi";
import MediaCard from "../../molecules/MediaCard/MediaCard";
import Badge from "../../atoms/Badge/Badge";
import Spinner from "../../atoms/Spinner/Spinner";
import { placeholderImage } from "../../../lib/placeholderImage";
import { formatDateRange } from "../../../lib/formatDate";
import styles from "./EventList.module.css";

export interface EventListProps {
  scope?: "all" | "upcoming";
  limit?: number;
  emptyMessage?: string;
}

export const EventList: React.FC<EventListProps> = ({
  scope = "all",
  limit,
  emptyMessage = "Aucun événement planifié pour le moment. Revenez bientôt !",
}) => {
  const { data: events, isLoading, isError } = useEvents();

  if (isLoading) return <Spinner label="Chargement des événements..." />;
  if (isError) return <p className={styles.error}>Impossible de charger les événements pour le moment.</p>;

  let filtered = events ?? [];
  if (scope === "upcoming") filtered = filtered.filter((e) => isUpcoming(e));
  filtered = [...filtered].sort((a, b) => a.startDateTime.localeCompare(b.startDateTime));
  if (limit) filtered = filtered.slice(0, limit);

  if (filtered.length === 0) return <p className={styles.empty}>{emptyMessage}</p>;

  return (
    <div className={styles.grid}>
      {filtered.map((event) => (
        <MediaCard
          key={event.eventId}
          to={`/evenements/${event.eventId}`}
          imageSrc={placeholderImage(event.eventId, event.name)}
          imageAlt={event.name}
          title={event.name}
          subtitle={formatDateRange(event.startDateTime, event.endDateTime)}
          excerpt={event.description}
          badge={<Badge label={isUpcoming(event) ? "À venir" : "Terminé"} tone={isUpcoming(event) ? "accent" : "muted"} />}
        />
      ))}
    </div>
  );
};

export default EventList;
