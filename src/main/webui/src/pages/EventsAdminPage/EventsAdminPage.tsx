import React from "react";
import { useAuth } from "../../auth/AuthContext";
import { useEventMutations, useEvents } from "../../features/events/useEvents";
import { EventInput } from "../../features/events/eventsApi";
import { Event } from "../../features/events/types";
import { formatDateRange } from "../../lib/formatDate";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Spinner from "../../components/atoms/Spinner/Spinner";

const emptyDraft: EventInput = {
  name: "",
  description: "",
  startDateTime: "",
  endDateTime: "",
  imageUrl: "",
  address: "",
  city: "",
  postalCode: "",
  country: "France",
};

const toDraft = (event: Event): EventInput => ({
  name: event.name,
  description: event.description,
  startDateTime: event.startDateTime,
  endDateTime: event.endDateTime,
  imageUrl: event.imageUrl ?? "",
  address: event.address ?? "",
  city: event.city ?? "",
  postalCode: event.postalCode ?? "",
  country: event.country ?? "France",
});

const renderFields = (value: EventInput, onChange: (value: EventInput) => void) => (
  <>
    <FormField label="Nom" value={value.name} onChange={(e) => onChange({ ...value, name: e.target.value })} required />
    <FormField label="Description" multiline rows={3} value={value.description} onChange={(e) => onChange({ ...value, description: e.target.value })} required />
    <FormField
      label="Début"
      type="datetime-local"
      value={value.startDateTime}
      onChange={(e) => onChange({ ...value, startDateTime: e.target.value })}
      required
    />
    <FormField
      label="Fin"
      type="datetime-local"
      value={value.endDateTime}
      onChange={(e) => onChange({ ...value, endDateTime: e.target.value })}
      required
    />
    <FormField label="Image (URL)" value={value.imageUrl} onChange={(e) => onChange({ ...value, imageUrl: e.target.value })} />
    <FormField label="Adresse" value={value.address} onChange={(e) => onChange({ ...value, address: e.target.value })} />
    <FormField label="Ville" value={value.city} onChange={(e) => onChange({ ...value, city: e.target.value })} />
    <FormField label="Code postal" value={value.postalCode} onChange={(e) => onChange({ ...value, postalCode: e.target.value })} />
  </>
);

export const EventsAdminPage: React.FC = () => {
  const { user } = useAuth();
  const { data: events, isLoading } = useEvents();
  const { update, create, remove } = useEventMutations();

  if (isLoading) return <Spinner label="Chargement des événements..." />;

  return (
    <AdminCrudList
      title="Gestion des événements"
      items={events ?? []}
      idOf={(e) => e.eventId}
      display={(e) => ({ title: e.name, subtitle: formatDateRange(e.startDateTime, e.endDateTime) })}
      toDraft={toDraft}
      renderFields={renderFields}
      onUpdate={(eventId, draft) => update.mutateAsync({ eventId, ...draft })}
      create={{
        buttonLabel: "+ Nouvel événement",
        submitLabel: "Créer l'événement",
        emptyDraft,
        onCreate: async (draft) => {
          if (!user) throw new Error("Utilisateur non connecté");
          await create.mutateAsync({ data: draft, creator: { userId: user.userId, firstName: user.firstName, lastName: user.lastName } });
        },
      }}
      remove={{
        title: "Supprimer cet événement ?",
        message: (e) => `L'événement « ${e.name} » sera définitivement supprimé, y compris pour les personnes déjà inscrites.`,
        onRemove: (eventId) => remove.mutateAsync(eventId),
      }}
    />
  );
};

export default EventsAdminPage;
