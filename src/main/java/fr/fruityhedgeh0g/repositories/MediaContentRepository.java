package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.medias.MediaContentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class MediaContentRepository implements PanacheRepositoryBase<MediaContentEntity, UUID> {
}
