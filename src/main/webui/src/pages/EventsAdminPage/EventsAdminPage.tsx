import React, { useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { useEventMutations, useEvents } from "../../features/events/useEvents";
import { EventInput } from "../../features/events/eventsApi";
import { formatDateRange } from "../../lib/formatDate";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import ConfirmDialog from "../../components/molecules/ConfirmDialog/ConfirmDialog";
import styles from "./EventsAdminPage.module.css";

const emptyDraft: EventInput = {
  name: "",
  description: "",
  startDateTime: "",
  endDateTime: "",
  address: "",
  city: "",
  postalCode: "",
  country: "France",
};

interface EventFormProps {
  value: EventInput;
  onChange: (value: EventInput) => void;
  onSubmit: (e: React.FormEvent) => void;
  submitLabel: string;
  pending: boolean;
  onDelete?: () => void;
}

const EventForm: React.FC<EventFormProps> = ({ value, onChange, onSubmit, submitLabel, pending, onDelete }) => (
  <form className={styles.form} onSubmit={onSubmit} noValidate>
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
    <FormField label="Adresse" value={value.address} onChange={(e) => onChange({ ...value, address: e.target.value })} />
    <FormField label="Ville" value={value.city} onChange={(e) => onChange({ ...value, city: e.target.value })} />
    <FormField label="Code postal" value={value.postalCode} onChange={(e) => onChange({ ...value, postalCode: e.target.value })} />
    <div className={styles.formActions}>
      <Button type="submit" label={submitLabel} disabled={pending} />
      {onDelete && <Button type="button" label="Supprimer" variant="danger" onClick={onDelete} disabled={pending} />}
    </div>
  </form>
);

export const EventsAdminPage: React.FC = () => {
  const { user } = useAuth();
  const { data: events, isLoading } = useEvents();
  const { update, create, remove } = useEventMutations();
  const [editingId, setEditingId] = useState<string | null>(null);
  const [draft, setDraft] = useState<EventInput>(emptyDraft);
  const [creating, setCreating] = useState(false);
  const [newEvent, setNewEvent] = useState<EventInput>(emptyDraft);
  const [toDeleteId, setToDeleteId] = useState<string | null>(null);

  if (isLoading) return <Spinner label="Chargement des événements..." />;

  const startEdit = (eventId: string) => {
    const event = events?.find((e) => e.eventId === eventId);
    if (!event) return;
    setEditingId(eventId);
    setDraft({
      name: event.name,
      description: event.description,
      startDateTime: event.startDateTime,
      endDateTime: event.endDateTime,
      address: event.address ?? "",
      city: event.city ?? "",
      postalCode: event.postalCode ?? "",
      country: event.country ?? "France",
    });
  };

  const handleSave = (eventId: string) => (e: React.FormEvent) => {
    e.preventDefault();
    update.mutate({ eventId, ...draft }, { onSuccess: () => setEditingId(null) });
  };

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    create.mutate(
      { data: newEvent, creator: { userId: user.userId, firstName: user.firstName, lastName: user.lastName } },
      {
        onSuccess: () => {
          setNewEvent(emptyDraft);
          setCreating(false);
        },
      }
    );
  };

  const toDelete = events?.find((e) => e.eventId === toDeleteId);

  return (
    <div className={styles.wrapper}>
      <div className={styles.toolbar}>
        <h2 className={styles.title}>Gestion des événements</h2>
        <Button label={creating ? "Annuler" : "+ Nouvel événement"} variant={creating ? "outline" : "primary"} onClick={() => setCreating((v) => !v)} />
      </div>

      {creating && <EventForm value={newEvent} onChange={setNewEvent} onSubmit={handleCreate} submitLabel="Créer l'événement" pending={create.isPending} />}

      <ul className={styles.list}>
        {events?.map((event) => (
          <AdminListItem
            key={event.eventId}
            title={event.name}
            subtitle={formatDateRange(event.startDateTime, event.endDateTime)}
            editing={editingId === event.eventId}
            onToggleEdit={() => (editingId === event.eventId ? setEditingId(null) : startEdit(event.eventId))}
          >
            <EventForm
              value={draft}
              onChange={setDraft}
              onSubmit={handleSave(event.eventId)}
              submitLabel="Enregistrer"
              pending={update.isPending}
              onDelete={() => setToDeleteId(event.eventId)}
            />
          </AdminListItem>
        ))}
      </ul>

      <ConfirmDialog
        isOpen={toDeleteId !== null}
        title="Supprimer cet événement ?"
        message={toDelete ? `L'événement « ${toDelete.name} » sera définitivement supprimé, y compris pour les personnes déjà inscrites.` : ""}
        pending={remove.isPending}
        onCancel={() => setToDeleteId(null)}
        onConfirm={() => {
          if (!toDeleteId) return;
          remove.mutate(toDeleteId, {
            onSuccess: () => {
              setToDeleteId(null);
              setEditingId(null);
            },
          });
        }}
      />
    </div>
  );
};

export default EventsAdminPage;
