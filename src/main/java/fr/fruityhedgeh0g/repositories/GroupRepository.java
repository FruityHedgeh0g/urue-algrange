package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.GroupEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.stream.Collectors;

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

    public Set<GroupEntity> findBySector(UUID sectorId){
        return new HashSet<>(list("sector_id", sectorId));
    }

    public int update(GroupEntity entity){
        return update("name = :name, description = :description, sector_id = :sectorId where group_id = :groupId", Parameters
                .with("name", entity.getName())
                .and("description", entity.getDescription())
                .and("sector_id", entity.getSector())
                .and("group_id", entity.getGroupId()));
    }
}
