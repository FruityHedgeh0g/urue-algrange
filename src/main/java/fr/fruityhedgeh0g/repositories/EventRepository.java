package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import fr.fruityhedgeh0g.enums.EventStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class EventRepository implements PanacheRepositoryBase<EventEntity, UUID> {

//    public EventEntity findByName(String name) {
//        return find("name", name).firstResult();
//    }

    /** Every Event except those in Planification: what Visiteurs and Roles below the Bureau may see. */
    public List<EventEntity> listOutsidePlanification() {
        return list("status != ?1", EventStatusEnum.PLANIFICATION);
    }

    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public Optional<EventEntity> findByName(String name) {
        return Optional.ofNullable(find("name", name)
                .firstResult());
    }
}
