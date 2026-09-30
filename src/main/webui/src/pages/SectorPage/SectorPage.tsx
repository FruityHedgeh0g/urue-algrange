import React, { useEffect, useState } from "react";
import { useSector, useSectorMutations, useSectors } from "../../features/sectors/useSector";
import { useGroups } from "../../features/groups/useGroups";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./SectorPage.module.css";

/**
 * Mon secteur (Bureau) : le Secteur — le premier, ou celui choisi s'il y en a
 * plusieurs —, sa description, que le Bureau tient à jour (seul le Super admin
 * le renomme), et ses Groupes, avec leur Chef de groupe et la partie du Secteur
 * qu'ils couvrent. Qui roule avec un Groupe se décide à chaque Événement.
 */
export const SectorPage: React.FC = () => {
  const { data: sectors, isLoading: sectorsLoading } = useSectors();
  const [chosenId, setChosenId] = useState<string>();
  const sectorId = chosenId ?? sectors?.[0]?.sectorId;
  const { data: sector, isLoading, isError } = useSector(sectorId);
  const { data: groups, isLoading: groupsLoading } = useGroups();
  const { update: updateSector } = useSectorMutations();

  const [description, setDescription] = useState("");
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    if (sector) {
      setDescription(sector.description);
      setSaved(false);
    }
  }, [sector]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!sectorId) return;
    updateSector.mutate({ sectorId, name: sector?.name ?? "", description }, { onSuccess: () => setSaved(true) });
  };

  if (sectorsLoading || isLoading) return <Spinner label="Chargement du secteur..." />;
  if (!sectorId) return <p className={styles.empty}>Aucun secteur pour le moment.</p>;
  if (isError || !sector) return <p className={styles.error}>Impossible de charger le secteur.</p>;

  const sectorGroups = (groups ?? []).filter((g) => g.sectorId === sector.sectorId);

  return (
    <div className={styles.wrapper}>
      <section className={styles.panel}>
        <h2>{sector.name}</h2>
        {(sectors?.length ?? 0) > 1 && (
          <Select
            label="Secteur"
            value={sector.sectorId}
            onChange={setChosenId}
            options={(sectors ?? []).map((s) => ({ value: s.sectorId, label: s.name }))}
          />
        )}
        <form className={styles.form} onSubmit={handleSubmit} noValidate>
          <FormField
            label="Description"
            multiline
            rows={4}
            value={description}
            onChange={(e) => {
              setDescription(e.target.value);
              setSaved(false);
            }}
          />
          <Button type="submit" label={updateSector.isPending ? "Enregistrement..." : "Enregistrer"} disabled={updateSector.isPending} />
          {saved && <p className={styles.saved}>Les informations du secteur ont été mises à jour.</p>}
        </form>
      </section>

      <section className={styles.panel}>
        <h2>Groupes du secteur</h2>
        {groupsLoading ? (
          <Spinner label="Chargement des groupes..." />
        ) : sectorGroups.length === 0 ? (
          <p className={styles.empty}>Aucun groupe dans ce secteur pour le moment.</p>
        ) : (
          <ul className={styles.groupList} aria-label="Groupes du secteur">
            {sectorGroups.map((group) => (
              <li key={group.groupId} className={styles.group}>
                <span className={styles.groupName}>{group.name}</span>
                <span className={styles.groupMeta}>
                  {[group.area, group.chef ? `Chef : ${group.chef.firstName} ${group.chef.lastName}` : "Sans chef de groupe"]
                    .filter(Boolean)
                    .join(" · ")}
                </span>
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
};

export default SectorPage;
