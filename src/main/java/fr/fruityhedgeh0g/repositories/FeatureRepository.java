package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class FeatureRepository implements PanacheRepositoryBase<FeatureEntity, String> {

}
