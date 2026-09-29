import React from "react";
import { useEventMutations, useEvents } from "../../features/events/useEvents";
import { EventInput } from "../../features/events/eventsApi";
import { allowedTransitions, EVENT_STATUS_LABELS, EventStatus } from "../../features/events/status";
import { Event } from "../../features/events/types";
import { useSectors } from "../../features/sectors/useSector";
import { formatDateRange } from "../../lib/formatDate";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";

/** Le statut n'est modifiable qu'à l'édition, vers les seules transitions permises. */
/** `maxParticipants` vide : pas de maximum. */
type EventDraft = Omit<EventInput, "maxParticipants"> & { status: EventStatus; maxParticipants: string };

const toDraft = (event: Event): EventDraft => ({
  name: event.name,
  description: event.description,
  startDateTime: event.startDateTime,
  endDateTime: event.endDateTime,
  sectorId: event.sectorId,
  status: event.status,
  maxParticipants: event.maxParticipants ? String(event.maxParticipants) : "",
  imageUrl: event.imageUrl ?? "",
  address: event.address ?? "",
  city: event.city ?? "",
  postalCode: event.postalCode ?? "",
  country: event.country ?? "France",
});

export const EventsAdminPage: React.FC = () => {
  const { data: events, isLoading } = useEvents();
  const { data: sectors } = useSectors();
  const { update, create, moveTo } = useEventMutations();

  const sectorOptions = (sectors ?? []).map((s) => ({ value: s.sectorId, label: s.name }));
  const emptyDraft: EventDraft = {
    name: "",
    description: "",
    startDateTime: "",
    endDateTime: "",
    sectorId: sectorOptions[0]?.value ?? "",
    status: "planification",
    maxParticipants: "",
    imageUrl: "",
    address: "",
    city: "",
    postalCode: "",
    country: "France",
  };

  const save = async (eventId: string, { status, sectorId: _sectorId, maxParticipants, ...patch }: EventDraft) => {
    await update.mutateAsync({ eventId, ...patch, maxParticipants: Number(maxParticipants) || 0 });
    const current = events?.find((e) => e.eventId === eventId)?.status;
    if (status !== current) await moveTo.mutateAsync({ eventId, status });
  };

  if (isLoading) return <Spinner label="Chargement des événements..." />;

  return (
    <AdminCrudList
      title="Gestion des événements"
      hint="Un événement n'est jamais supprimé : annulez-le. En cours et Archivé suivent les dates."
      items={events ?? []}
      idOf={(e) => e.eventId}
      display={(e) => ({
        title: e.name,
        subtitle: [
          EVENT_STATUS_LABELS[e.status],
          formatDateRange(e.startDateTime, e.endDateTime),
          e.maxParticipants ? `${e.maxParticipants} places` : undefined,
        ]
          .filter(Boolean)
          .join(" · "),
      })}
      toDraft={toDraft}
      renderFields={(value, onChange, event) => {
        const transitions = event ? allowedTransitions(event.status) : [];
        return (
          <>
            <FormField label="Nom" value={value.name} onChange={(e) => onChange({ ...value, name: e.target.value })} required />
            <FormField
              label="Description"
              multiline
              rows={3}
              value={value.description}
              onChange={(e) => onChange({ ...value, description: e.target.value })}
            />
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
            <FormField
              label="Participants maximum"
              type="number"
              min={1}
              value={value.maxParticipants}
              onChange={(e) => onChange({ ...value, maxParticipants: e.target.value })}
            />
            {!event && (
              <Select label="Secteur" value={value.sectorId} onChange={(sectorId) => onChange({ ...value, sectorId })} options={sectorOptions} />
            )}
            {event && transitions.length > 0 && (
              <Select
                label="Statut"
                value={value.status}
                onChange={(status) => onChange({ ...value, status: status as EventStatus })}
                options={[event.status, ...transitions].map((s) => ({ value: s, label: EVENT_STATUS_LABELS[s] }))}
              />
            )}
            <FormField label="Image (URL)" value={value.imageUrl} onChange={(e) => onChange({ ...value, imageUrl: e.target.value })} />
            <FormField label="Adresse" value={value.address} onChange={(e) => onChange({ ...value, address: e.target.value })} />
            <FormField label="Ville" value={value.city} onChange={(e) => onChange({ ...value, city: e.target.value })} />
            <FormField label="Code postal" value={value.postalCode} onChange={(e) => onChange({ ...value, postalCode: e.target.value })} />
          </>
        );
      }}
      onUpdate={save}
      create={{
        buttonLabel: "+ Nouvel événement",
        submitLabel: "Créer l'événement",
        emptyDraft,
        onCreate: ({ status: _status, maxParticipants, ...input }) =>
          create.mutateAsync({ ...input, maxParticipants: Number(maxParticipants) || undefined }),
      }}
    />
  );
};

export default EventsAdminPage;
