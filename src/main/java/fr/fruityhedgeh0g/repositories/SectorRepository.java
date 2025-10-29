package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.SectorEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SectorRepository implements PanacheRepositoryBase<SectorEntity, UUID> {
    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public Optional<SectorEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }

    public boolean existsById(UUID id) {
        return Optional.ofNullable(findById(id)).isPresent();
    }
}
