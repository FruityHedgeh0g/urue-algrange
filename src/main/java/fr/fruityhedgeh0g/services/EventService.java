package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@ApplicationScoped
public class EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    EventMapper eventMapper;
}
