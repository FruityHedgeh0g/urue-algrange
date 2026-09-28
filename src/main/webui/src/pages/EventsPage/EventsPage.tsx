import React from "react";
import EventList from "../../components/organisms/EventList/EventList";
import PageHero from "../../components/organisms/PageHero/PageHero";

export const EventsPage: React.FC = () => (
  <>
    <PageHero
      eyebrow="Nos actions"
      title="Événements"
      lead="Retrouvez toutes nos collectes et actions, à venir ou passées."
    />
    <div className="container">
      <EventList scope="all" />
    </div>
  </>
);

export default EventsPage;
