package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.medias.MediaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MediaRepository implements PanacheRepositoryBase<MediaEntity, UUID> {
    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public Optional<MediaEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }
}
