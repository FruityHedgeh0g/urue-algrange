import React from "react";
import { useFeatureFlags, useSetFeatureFlagActive } from "../../features/featureFlags/useFeatureFlags";
import Badge from "../../components/atoms/Badge/Badge";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./FeatureFlagsPage.module.css";

export const FeatureFlagsPage: React.FC = () => {
  const { data: flags, isLoading } = useFeatureFlags();
  const setActive = useSetFeatureFlagActive();

  if (isLoading) return <Spinner label="Chargement des fonctionnalités..." />;

  return (
    <ul className={styles.list}>
      {flags?.map((flag) => (
        <li key={flag.name} className={styles.item}>
          <div>
            <div className={styles.titleLine}>
              <span className={styles.title}>{flag.name}</span>
              <Badge label={flag.isActive ? "Active" : "Inactive"} tone={flag.isActive ? "accent" : "muted"} />
            </div>
            <p className={styles.description}>{flag.description}</p>
          </div>
          <Button
            label={flag.isActive ? "Désactiver" : "Activer"}
            variant={flag.isActive ? "outline" : "accent"}
            disabled={setActive.isPending}
            onClick={() => setActive.mutate({ name: flag.name, isActive: !flag.isActive })}
          />
        </li>
      ))}
    </ul>
  );
};

export default FeatureFlagsPage;
