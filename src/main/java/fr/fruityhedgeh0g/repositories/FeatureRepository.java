package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.model.entities.configurations.FeatureEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FeatureRepository implements PanacheRepositoryBase<FeatureEntity, String> {

//    public Optional<FeatureEntity> findByName(String name) {
//        return Optional.ofNullable(find("name", name)
//                .firstResult());
//    }
}
