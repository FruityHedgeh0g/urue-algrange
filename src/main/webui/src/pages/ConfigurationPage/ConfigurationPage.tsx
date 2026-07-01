import React, { useState } from "react";
import { useConfigurations, useUpdateConfiguration } from "../../features/configurations/useConfigurations";
import { NAVBAR_LOGO_CONFIG_NAME, resolveNavbarLogo } from "../../features/configurations/navbarLogo";
import { useMedias } from "../../features/medias/useMedias";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./ConfigurationPage.module.css";

export const ConfigurationPage: React.FC = () => {
  const { data: configurations, isLoading } = useConfigurations();
  const { data: medias, isLoading: mediasLoading } = useMedias();
  const updateConfiguration = useUpdateConfiguration();
  const [editingName, setEditingName] = useState<string | null>(null);
  const [value, setValue] = useState("");

  if (isLoading || mediasLoading) return <Spinner label="Chargement de la configuration..." />;

  const logoOptions = [
    { value: "", label: "Logo de l'association (par défaut)" },
    ...(medias ?? []).map((m) => ({ value: m.mediaId, label: m.alt })),
  ];

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
      {configurations?.map((config) => {
        const isLogo = config.name === NAVBAR_LOGO_CONFIG_NAME;
        const logo = resolveNavbarLogo(config.value, medias);
        return (
          <AdminListItem
            key={config.name}
            title={config.name}
            subtitle={isLogo ? (config.value ? logo.alt : "Logo par défaut") : config.value}
            leading={isLogo ? <img className={styles.thumb} src={logo.src} alt="" /> : undefined}
            editing={editingName === config.name}
            onToggleEdit={() => (editingName === config.name ? setEditingName(null) : startEdit(config.name, config.value))}
          >
            <form className={styles.form} onSubmit={handleSave(config.name)} noValidate>
              {isLogo ? (
                <Select label="Logo" value={value} onChange={setValue} options={logoOptions} />
              ) : (
                <FormField label="Valeur" value={value} onChange={(e) => setValue(e.target.value)} required />
              )}
              <Button type="submit" label="Enregistrer" disabled={updateConfiguration.isPending} />
            </form>
          </AdminListItem>
        );
      })}
    </ul>
  );
};

export default ConfigurationPage;
