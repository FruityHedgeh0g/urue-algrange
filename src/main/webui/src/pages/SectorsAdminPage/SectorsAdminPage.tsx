import React, { useState } from "react";
import { useSectorMutations, useSectors } from "../../features/sectors/useSector";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./SectorsAdminPage.module.css";

interface Draft {
  name: string;
  description: string;
}

const emptyDraft: Draft = { name: "", description: "" };

export const SectorsAdminPage: React.FC = () => {
  const { data: sectors, isLoading } = useSectors();
  const { update, create } = useSectorMutations();
  const [editingId, setEditingId] = useState<string | null>(null);
  const [draft, setDraft] = useState<Draft>(emptyDraft);
  const [creating, setCreating] = useState(false);
  const [newSector, setNewSector] = useState<Draft>(emptyDraft);

  if (isLoading) return <Spinner label="Chargement des secteurs..." />;

  const startEdit = (sectorId: string) => {
    const sector = sectors?.find((s) => s.sectorId === sectorId);
    if (!sector) return;
    setEditingId(sectorId);
    setDraft({ name: sector.name, description: sector.description });
  };

  const handleSave = (sectorId: string) => (e: React.FormEvent) => {
    e.preventDefault();
    update.mutate({ sectorId, ...draft }, { onSuccess: () => setEditingId(null) });
  };

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    create.mutate(newSector, {
      onSuccess: () => {
        setNewSector(emptyDraft);
        setCreating(false);
      },
    });
  };

  return (
    <div className={styles.wrapper}>
      <Button label={creating ? "Annuler" : "Nouveau secteur"} variant={creating ? "outline" : "accent"} onClick={() => setCreating((v) => !v)} />

      {creating && (
        <form className={styles.form} onSubmit={handleCreate} noValidate>
          <FormField label="Nom du secteur" value={newSector.name} onChange={(e) => setNewSector({ ...newSector, name: e.target.value })} required />
          <FormField
            label="Description"
            multiline
            rows={3}
            value={newSector.description}
            onChange={(e) => setNewSector({ ...newSector, description: e.target.value })}
          />
          <Button type="submit" label="Créer le secteur" disabled={create.isPending} />
        </form>
      )}

      <ul className={styles.list}>
        {sectors?.map((sector) => (
          <AdminListItem
            key={sector.sectorId}
            title={sector.name}
            subtitle={`${sector.groups.length} groupe(s)`}
            editing={editingId === sector.sectorId}
            onToggleEdit={() => (editingId === sector.sectorId ? setEditingId(null) : startEdit(sector.sectorId))}
          >
            <form className={styles.form} onSubmit={handleSave(sector.sectorId)} noValidate>
              <FormField label="Nom du secteur" value={draft.name} onChange={(e) => setDraft({ ...draft, name: e.target.value })} required />
              <FormField
                label="Description"
                multiline
                rows={3}
                value={draft.description}
                onChange={(e) => setDraft({ ...draft, description: e.target.value })}
              />
              <Button type="submit" label="Enregistrer" disabled={update.isPending} />
            </form>
          </AdminListItem>
        ))}
      </ul>
    </div>
  );
};

export default SectorsAdminPage;
