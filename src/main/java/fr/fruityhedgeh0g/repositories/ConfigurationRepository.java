package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ConfigurationRepository implements PanacheRepository<ConfigurationEntity> {

    public Optional<ConfigurationEntity> findConfigurationByName(String name) {
        return Optional.ofNullable(find("name", name).firstResult());
    }

    public List<ConfigurationEntity> findAllConfigurations() {
        return listAll();
    }

    public int updateConfiguration(ConfigurationEntity entity) {
        return update("value = :value where name = :name", Parameters
                .with("value", entity.getValue())
                .and("name", entity.getName()));

    }

}
