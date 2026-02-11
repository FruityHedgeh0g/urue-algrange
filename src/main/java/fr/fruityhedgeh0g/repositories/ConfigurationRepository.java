package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationRepository implements PanacheRepositoryBase<ConfigurationEntity, String> {

//    public Optional<ConfigurationEntity> findConfigurationByName(String name) {
//        return Optional.ofNullable(find("name", name)
//                .firstResult());
//    }


}
