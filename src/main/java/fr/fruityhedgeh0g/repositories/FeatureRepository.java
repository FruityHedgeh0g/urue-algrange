package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeatureRepository implements PanacheRepositoryBase<FeatureEntity, String> {

//    public Optional<FeatureEntity> findByName(String name) {
//        return Optional.ofNullable(find("name", name)
//                .firstResult());
//    }
}
