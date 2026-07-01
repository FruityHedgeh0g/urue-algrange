import React, { useState } from "react";
import { useConfigurations, useUpdateConfiguration } from "../../features/configurations/useConfigurations";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./ConfigurationPage.module.css";

export const ConfigurationPage: React.FC = () => {
  const { data: configurations, isLoading } = useConfigurations();
  const updateConfiguration = useUpdateConfiguration();
  const [editingName, setEditingName] = useState<string | null>(null);
  const [value, setValue] = useState("");

  if (isLoading) return <Spinner label="Chargement de la configuration..." />;

  const startEdit = (name: string, currentValue: string) => {
    setEditingName(name);
    setValue(currentValue);
  };

  const handleSave = (name: string) => (e: React.FormEvent) => {
    e.preventDefault();
    updateConfiguration.mutate({ name, value }, { onSuccess: () => setEditingName(null) });
  };

  return (
    <ul className={styles.list}>
      {configurations?.map((config) => (
        <AdminListItem
          key={config.name}
          title={config.name}
          subtitle={config.value}
          editing={editingName === config.name}
          onToggleEdit={() => (editingName === config.name ? setEditingName(null) : startEdit(config.name, config.value))}
        >
          <form className={styles.form} onSubmit={handleSave(config.name)} noValidate>
            <FormField label="Valeur" value={value} onChange={(e) => setValue(e.target.value)} required />
            <Button type="submit" label="Enregistrer" disabled={updateConfiguration.isPending} />
          </form>
        </AdminListItem>
      ))}
    </ul>
  );
};

export default ConfigurationPage;
