package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
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

    public List<RoleEntity> findByType(RoleTypeEnum[] filter) {
        return find("roleType", (Object[]) filter)
                .list();
    }
}
