import React from "react";
import EventList from "../../components/organisms/EventList/EventList";

export const EventsPage: React.FC = () => (
  <div className="container">
    <h1>Événements</h1>
    <p>Retrouvez toutes nos collectes et actions, à venir ou passées.</p>
    <EventList scope="all" />
  </div>
);

export default EventsPage;
