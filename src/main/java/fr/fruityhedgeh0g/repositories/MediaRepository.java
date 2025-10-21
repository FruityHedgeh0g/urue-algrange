package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.model.entities.medias.MediaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class MediaRepository implements PanacheRepositoryBase<MediaEntity, UUID> {
}
