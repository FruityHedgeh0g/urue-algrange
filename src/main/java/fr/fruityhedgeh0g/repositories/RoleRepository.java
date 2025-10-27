package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.roles.RoleEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<RoleEntity, UUID> {
    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public Optional<RoleEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }
}
