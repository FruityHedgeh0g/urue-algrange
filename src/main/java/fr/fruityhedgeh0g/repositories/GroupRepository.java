package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.GroupEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

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

    public Optional<Set<GroupEntity>> findBySector(UUID sectorId){
        return Optional.of(new HashSet<>(list("sector_id", sectorId)));
    }

}
