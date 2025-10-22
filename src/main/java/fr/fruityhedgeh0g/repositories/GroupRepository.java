package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.GroupEntity;
import fr.fruityhedgeh0g.model.entities.SeriesEntity;
import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GroupRepository implements PanacheRepositoryBase<GroupEntity, UUID> {
    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public Optional<GroupEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }
}
