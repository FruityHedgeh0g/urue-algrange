package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class EventServiceImpl implements EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    EventMapper eventMapper;

    @Transactional
    public Try<List<EventDto>> getAllEvents(){
        return null;
    }

    @Override
    public Try<List<EventDto>> getAllEventsFiltered(String filter) {
        return null;
    }

    @Transactional
    public Try<EventDto> getEventById( UUID eventId){
        return null;
    }

    @Transactional
    public Try<EventDto> createEvent( EventDto eventDto){
        return null;
    }

    //TODO : Développer l'update
    @Transactional
    public Try<EventDto> updateEvent( EventDto eventDto){
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    @Transactional
    public Try<Void> deleteEvent( UUID eventId){
        return null;
    }

    @Override
    public Try<EventDto> addParticipant(UUID eventId, UUID userId) {
        return null;
    }

    @Override
    public Try<EventDto> removeParticipant(UUID eventId, UUID userId) {
        return null;
    }

    @Override
    public Try<EventDto> addOrganizer(UUID eventId, UUID userId) {
        return null;
    }

    @Override
    public Try<EventDto> removeOrganizer(UUID eventId, UUID userId) {
        return null;
    }
}
