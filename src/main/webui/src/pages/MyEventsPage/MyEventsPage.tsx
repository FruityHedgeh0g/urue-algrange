import React from "react";
import { Link } from "react-router-dom";
import { useEvents } from "../../features/events/useEvents";
import { useMyEventIds } from "../../features/events/useMyRegistrations";
import { isUpcoming } from "../../features/events/eventsApi";
import { formatDateRange } from "../../lib/formatDate";
import MediaCard from "../../components/molecules/MediaCard/MediaCard";
import Badge from "../../components/atoms/Badge/Badge";
import Spinner from "../../components/atoms/Spinner/Spinner";
import { placeholderImage } from "../../lib/placeholderImage";
import styles from "./MyEventsPage.module.css";

export const MyEventsPage: React.FC = () => {
  const { data: events, isLoading: eventsLoading } = useEvents();
  const { data: myEventIds, isLoading: idsLoading } = useMyEventIds();

  if (eventsLoading || idsLoading) return <Spinner label="Chargement de vos événements..." />;

  const myEvents = (events ?? []).filter((e) => myEventIds?.includes(e.eventId));

  if (myEvents.length === 0) {
    return (
      <p className={styles.empty}>
        Vous n'êtes inscrit à aucun événement pour le moment. <Link to="/evenements">Découvrir les événements</Link>
      </p>
    );
  }

  return (
    <div className={styles.grid}>
      {myEvents.map((event) => (
        <MediaCard
          key={event.eventId}
          to={`/evenements/${event.eventId}`}
          imageSrc={placeholderImage(event.eventId, event.name)}
          imageAlt={event.name}
          title={event.name}
          subtitle={formatDateRange(event.startDateTime, event.endDateTime)}
          badge={<Badge label={isUpcoming(event) ? "À venir" : "Terminé"} tone={isUpcoming(event) ? "accent" : "muted"} />}
        />
      ))}
    </div>
  );
};

export default MyEventsPage;
