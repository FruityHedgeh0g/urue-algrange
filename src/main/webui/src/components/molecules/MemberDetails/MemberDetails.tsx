import React from "react";
import { Member } from "../../../features/users/types";
import { formatDate } from "../../../lib/formatDate";
import Badge from "../../atoms/Badge/Badge";
import styles from "./MemberDetails.module.css";

export interface MemberDetailsProps {
  member: Member;
  groupName?: string;
  sectorName?: string;
}

/** Fiche détaillée d'un inscrit, affichée par exemple dans une modale. */
export const MemberDetails: React.FC<MemberDetailsProps> = ({ member, groupName, sectorName }) => (
  <div className={styles.details}>
    <Badge label={member.role} />
    <dl className={styles.list}>
      <div className={styles.row}>
        <dt className={styles.term}>E-mail</dt>
        <dd className={styles.value}>
          <a href={`mailto:${member.email}`}>{member.email}</a>
        </dd>
      </div>
      <div className={styles.row}>
        <dt className={styles.term}>Téléphone</dt>
        <dd className={styles.value}>
          <a href={`tel:${member.phone.replace(/\s/g, "")}`}>{member.phone}</a>
        </dd>
      </div>
      {groupName && (
        <div className={styles.row}>
          <dt className={styles.term}>Groupe</dt>
          <dd className={styles.value}>
            {groupName}
            {sectorName ? ` · ${sectorName}` : ""}
          </dd>
        </div>
      )}
      <div className={styles.row}>
        <dt className={styles.term}>Membre depuis</dt>
        <dd className={styles.value}>{formatDate(member.memberSince)}</dd>
      </div>
    </dl>
  </div>
);

export default MemberDetails;
