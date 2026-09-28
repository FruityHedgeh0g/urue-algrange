import React from "react";
import { useConfigurations, useUpdateConfiguration } from "../../features/configurations/useConfigurations";
import { NAVBAR_LOGO_CONFIG_NAME, resolveNavbarLogo } from "../../features/configurations/navbarLogo";
import { useMedias } from "../../features/medias/useMedias";
import { mediaOptions } from "../../features/medias/mediaImage";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./ConfigurationPage.module.css";

export const ConfigurationPage: React.FC = () => {
  const { data: configurations, isLoading } = useConfigurations();
  const { data: medias, isLoading: mediasLoading } = useMedias();
  const updateConfiguration = useUpdateConfiguration();

  if (isLoading || mediasLoading) return <Spinner label="Chargement de la configuration..." />;

  const logoOptions = mediaOptions(medias);

  return (
    <AdminCrudList
      items={configurations ?? []}
      idOf={(config) => config.name}
      display={(config) => {
        if (config.name !== NAVBAR_LOGO_CONFIG_NAME) return { title: config.name, subtitle: config.value };
        const logo = resolveNavbarLogo(config.value, medias);
        return {
          title: config.name,
          subtitle: logo.isDefault ? "Logo par défaut" : logo.alt,
          leading: <img className={styles.thumb} src={logo.src} alt="" />,
        };
      }}
      toDraft={(config) => config.value}
      renderFields={(value, setValue, config) =>
        config?.name === NAVBAR_LOGO_CONFIG_NAME ? (
          <Select label="Logo" value={value} onChange={setValue} options={logoOptions} />
        ) : (
          <FormField label="Valeur" value={value} onChange={(e) => setValue(e.target.value)} required />
        )
      }
      onUpdate={(name, value) => updateConfiguration.mutateAsync({ name, value })}
    />
  );
};

export default ConfigurationPage;
