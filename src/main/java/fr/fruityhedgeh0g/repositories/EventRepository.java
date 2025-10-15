package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventRepository implements PanacheRepository<EventEntity> {

//    public EventEntity findByName(String name) {
//        return find("name", name).firstResult();
//    }
}
