package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class EventRepository implements PanacheRepositoryBase<EventEntity, UUID> {

//    public EventEntity findByName(String name) {
//        return find("name", name).firstResult();
//    }

    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public Optional<EventEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }
}
