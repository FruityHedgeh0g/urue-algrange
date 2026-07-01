import React, { useEffect, useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import { useSector, useUpdateSector } from "../../features/sectors/useSector";
import { useMembersByGroupIds } from "../../features/users/useMembers";
import { Member } from "../../features/users/types";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import Modal from "../../components/molecules/Modal/Modal";
import MemberDetails from "../../components/molecules/MemberDetails/MemberDetails";
import styles from "./SectorPage.module.css";

export const SectorPage: React.FC = () => {
  const { user } = useAuth();
  const sectorId = user?.group.sectorId;
  const { data: sector, isLoading, isError } = useSector(sectorId);
  const updateSector = useUpdateSector(sectorId);

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [saved, setSaved] = useState(false);
  const [selectedMember, setSelectedMember] = useState<Member | null>(null);

  useEffect(() => {
    if (sector) {
      setName(sector.name);
      setDescription(sector.description);
    }
  }, [sector]);

  const groupIds = sector?.groups.map((g) => g.groupId) ?? [];
  const { data: members, isLoading: membersLoading } = useMembersByGroupIds(groupIds);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateSector.mutate(
      { name, description },
      {
        onSuccess: () => setSaved(true),
      }
    );
  };

  if (isLoading) return <Spinner label="Chargement de votre secteur..." />;
  if (isError || !sector) return <p className={styles.error}>Impossible de charger votre secteur.</p>;

  const selectedGroup = selectedMember ? sector.groups.find((g) => g.groupId === selectedMember.groupId) : undefined;

  return (
    <div className={styles.wrapper}>
      <section>
        <h2>Informations du secteur</h2>
        <form className={styles.form} onSubmit={handleSubmit} noValidate>
          <FormField
            label="Nom du secteur"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
              setSaved(false);
            }}
            required
          />
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

      <section>
        <h2>Inscrits de mon secteur</h2>
        {membersLoading && <Spinner label="Chargement des inscrits..." />}
        {!membersLoading && (!members || members.length === 0) && (
          <p className={styles.empty}>Aucun inscrit dans ce secteur pour le moment.</p>
        )}
        {members && members.length > 0 && (
          <ul className={styles.memberList}>
            {members.map((member) => {
              const group = sector.groups.find((g) => g.groupId === member.groupId);
              return (
                <li key={member.userId}>
                  <button type="button" className={styles.member} onClick={() => setSelectedMember(member)}>
                    <span>
                      {member.firstName} {member.lastName}
                    </span>
                    <span className={styles.memberMeta}>
                      {group && <span className={styles.memberGroup}>{group.name}</span>}
                      <span className={styles.memberArrow} aria-hidden>
                        ›
                      </span>
                    </span>
                  </button>
                </li>
              );
            })}
          </ul>
        )}
      </section>

      <Modal
        isOpen={selectedMember !== null}
        onClose={() => setSelectedMember(null)}
        title={selectedMember ? `${selectedMember.firstName} ${selectedMember.lastName}` : ""}
      >
        {selectedMember && <MemberDetails member={selectedMember} groupName={selectedGroup?.name} sectorName={sector.name} />}
      </Modal>
    </div>
  );
};

export default SectorPage;
