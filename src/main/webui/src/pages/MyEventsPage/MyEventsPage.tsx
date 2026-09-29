import React from "react";
import { useEvents } from "../../features/events/useEvents";
import { useMyEventIds } from "../../features/events/useMyRegistrations";
import { EVENT_STATUS_LABELS, statusTone } from "../../features/events/status";
import { formatDateRange } from "../../lib/formatDate";
import MediaCard from "../../components/molecules/MediaCard/MediaCard";
import Badge from "../../components/atoms/Badge/Badge";
import Spinner from "../../components/atoms/Spinner/Spinner";
import Icon from "../../components/atoms/Icon/Icon";
import ButtonLink from "../../components/atoms/ButtonLink/ButtonLink";
import { placeholderImage } from "../../lib/placeholderImage";
import styles from "./MyEventsPage.module.css";

export const MyEventsPage: React.FC = () => {
  const { data: events, isLoading: eventsLoading } = useEvents();
  const { data: myEventIds, isLoading: idsLoading } = useMyEventIds();

  if (eventsLoading || idsLoading) return <Spinner label="Chargement de vos événements..." />;

  const myEvents = (events ?? []).filter((e) => myEventIds?.includes(e.eventId));

  if (myEvents.length === 0) {
    return (
      <div className={styles.empty}>
        <span className={styles.emptyIcon} aria-hidden="true">
          <Icon name="calendar" size={30} />
        </span>
        <p>Vous n'êtes inscrit à aucun événement pour le moment.</p>
        <ButtonLink to="/evenements" label="Découvrir les événements" variant="accent" arrow />
      </div>
    );
  }

  return (
    <div className={styles.grid}>
      {myEvents.map((event) => (
        <MediaCard
          key={event.eventId}
          to={`/evenements/${event.eventId}`}
          imageSrc={event.imageUrl || placeholderImage(event.eventId, event.name)}
          imageAlt={event.name}
          title={event.name}
          subtitle={formatDateRange(event.startDateTime, event.endDateTime)}
          badge={<Badge label={EVENT_STATUS_LABELS[event.status]} tone={statusTone(event.status)} />}
        />
      ))}
    </div>
  );
};

export default MyEventsPage;
