package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.SeriesEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class SerieRepository implements PanacheRepositoryBase<SeriesEntity, UUID> {
}
