import React from "react";
import { useSectorMutations, useSectors } from "../../features/sectors/useSector";
import { SectorInput } from "../../features/sectors/sectorsApi";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Spinner from "../../components/atoms/Spinner/Spinner";

const emptyDraft: SectorInput = { name: "", description: "" };

export const SectorsAdminPage: React.FC = () => {
  const { data: sectors, isLoading } = useSectors();
  const { update, create, remove } = useSectorMutations();

  if (isLoading) return <Spinner label="Chargement des secteurs..." />;

  return (
    <AdminCrudList
      title="Secteurs"
      items={sectors ?? []}
      idOf={(s) => s.sectorId}
      display={(s) => ({ title: s.name, subtitle: `${s.groups.length} groupe(s)` })}
      toDraft={(s): SectorInput => ({ name: s.name, description: s.description })}
      renderFields={(draft, setDraft) => (
        <>
          <FormField label="Nom du secteur" value={draft.name} onChange={(e) => setDraft({ ...draft, name: e.target.value })} required />
          <FormField
            label="Description"
            multiline
            rows={3}
            value={draft.description}
            onChange={(e) => setDraft({ ...draft, description: e.target.value })}
          />
        </>
      )}
      onUpdate={(sectorId, draft) => update.mutateAsync({ sectorId, ...draft })}
      create={{
        buttonLabel: "+ Nouveau secteur",
        submitLabel: "Créer le secteur",
        emptyDraft,
        onCreate: (draft) => create.mutateAsync(draft),
      }}
      remove={{
        title: "Supprimer ce secteur ?",
        message: (s) => `Le secteur « ${s.name} » et son rattachement aux groupes seront supprimés. Cette action est irréversible.`,
        onRemove: (sectorId) => remove.mutateAsync(sectorId),
      }}
    />
  );
};

export default SectorsAdminPage;
