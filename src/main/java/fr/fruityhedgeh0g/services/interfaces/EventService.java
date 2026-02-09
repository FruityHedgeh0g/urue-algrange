package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface EventService {
    Try<List<EventDto>> getAllEvents();
    Try<List<EventDto>> getAllEventsFiltered(@NotNull String filter);
    Try<EventDto> getEventById(@NotNull UUID eventId);
    Try<EventDto> createEvent(@NotNull EventDto eventDto);
    Try<EventDto> updateEvent(@NotNull EventDto eventDto);
    Try<Void> deleteEvent(@NotNull UUID eventId);
    Try<EventDto> addParticipant(@NotNull UUID eventId, @NotNull UUID userId);
    Try<EventDto> removeParticipant(@NotNull UUID eventId, @NotNull UUID userId);
    Try<EventDto> addOrganizer(@NotNull UUID eventId, @NotNull UUID userId);
    Try<EventDto> removeOrganizer(@NotNull UUID eventId, @NotNull UUID userId);

}
