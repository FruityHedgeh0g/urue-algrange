import React from "react";
import { useFeatureFlags, useSetFeatureFlagActive } from "../../features/featureFlags/useFeatureFlags";
import { useAuth } from "../../auth/AuthContext";
import Badge from "../../components/atoms/Badge/Badge";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./FeatureFlagsPage.module.css";

export const FeatureFlagsPage: React.FC = () => {
  const { data: flags, isLoading } = useFeatureFlags();
  const setActive = useSetFeatureFlagActive();
  // Les Admins les voient ; seul le Super admin les change (#27)
  const switches = useAuth().hasAtLeastRole("super_admin");

  if (isLoading) return <Spinner label="Chargement des fonctionnalités..." />;

  return (
    <ul className={styles.list}>
      {flags?.map((flag) => (
        <li key={flag.name} className={`${styles.item}${flag.isActive ? ` ${styles.active}` : ""}`}>
          <div>
            <div className={styles.titleLine}>
              <span className={styles.title}>{flag.name}</span>
              <Badge label={flag.isActive ? "Active" : "Inactive"} tone={flag.isActive ? "accent" : "muted"} />
            </div>
            <p className={styles.description}>{flag.description}</p>
          </div>
          {switches && (
            <Button
              label={flag.isActive ? "Désactiver" : "Activer"}
              variant={flag.isActive ? "outline" : "accent"}
              disabled={setActive.isPending}
              onClick={() => setActive.mutate({ name: flag.name, isActive: !flag.isActive })}
            />
          )}
        </li>
      ))}
    </ul>
  );
};

export default FeatureFlagsPage;
