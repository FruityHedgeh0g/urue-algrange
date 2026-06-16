package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
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

    @Override
    public List<EventDto> listAll() {
        return eventRepository.listAll()
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Override
    public Optional<EventDto> getById(UUID eventId) {
        return null;
    }

    @Override
    public EventDto create(EventDto eventDto) {
        return null;
    }

    @Override
    public EventDto update(EventDto eventDto) {
        return null;
    }

    @Override
    public void delete(UUID eventId) {
    }

//    @Transactional
//    public Try<List<EventDto>> getAllEvents(){
//        Log.info("Getting all events");
//        return Try.of(() -> eventRepository
//                .findAll()
//                .stream()
//                .map(eventMapper::toDto)
//                .toList())
//                .onFailure(e -> Log.error("Error getting all events", e));
//    }
//
//    @Override
//    public Try<List<EventDto>> getAllEventsFiltered(String filter) {
//        return null;
//    }
//
//    @Transactional
//    public Try<EventDto> getEventById( UUID eventId){
//        Log.infof("Getting event with id: %s" , eventId);
//        return Try.of(() -> eventRepository.findByIdOptional(eventId)
//                .orElseThrow(() -> new UnknownResourceException("Event not found with id: " + eventId)))
//                .map(eventMapper::toDto)
//                .onFailure(ex -> {
//                    if (ex instanceof UnknownResourceException e) {
//                        Log.warn(e.getMessage());
//                    } else {
//                        Log.errorf(ex,"Error getting event with id: %s" , eventId);
//                    }
//                });
//    }
//
//    @Transactional
//    public Try<EventDto> createEvent( EventDto eventDto){
//        return null;
//    }
//
//    @Transactional
//    public Try<EventDto> updateEvent( EventDto eventDto){
//        return null;
//    }
//
//    @Transactional
//    public Try<Void> deleteEvent( UUID eventId){
//        return null;
//    }
//
//    @Override
//    public Try<EventDto> addParticipant(UUID eventId, UUID userId) {
//        return null;
//    }
//
//    @Override
//    public Try<EventDto> removeParticipant(UUID eventId, UUID userId) {
//        return null;
//    }
//
//    @Override
//    public Try<EventDto> addOrganizer(UUID eventId, UUID userId) {
//        return null;
//    }
//
//    @Override
//    public Try<EventDto> removeOrganizer(UUID eventId, UUID userId) {
//        return null;
//    }
}
