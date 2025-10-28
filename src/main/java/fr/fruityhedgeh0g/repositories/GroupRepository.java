package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.GroupEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class GroupRepository implements PanacheRepositoryBase<GroupEntity, UUID> {
    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public boolean existsById(UUID id) {
        return Optional.ofNullable(findById(id)).isPresent();
    }

    public Optional<GroupEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }

    public List<GroupEntity> findByIds(Set<UUID> ids) {
        return list("id in (?1)", ids);
    }
}
