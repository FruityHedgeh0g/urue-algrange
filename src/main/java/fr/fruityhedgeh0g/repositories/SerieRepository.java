package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.SerieEntity;
import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class SerieRepository implements PanacheRepositoryBase<SerieEntity, UUID> {
}
