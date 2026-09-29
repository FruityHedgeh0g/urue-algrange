package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.enums.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/** Events are never deleted: they are cancelled instead. */
public interface PublicEventService {
    /** @param seesPlanification true for the Bureau and above; others never see Planification Events */
    List<EventDto> listAll(boolean seesPlanification);
    EventDto getById(@NotNull UUID eventId, boolean seesPlanification);
    /** A new Event belongs to a Secteur and starts in Planification. */
    EventDto create(@NotNull @Valid EventDto eventDto);
    EventDto update(@NotNull @Valid EventDto eventDto);
    /** Manual transition, checked against the Event's current status. */
    EventDto changeStatus(@NotNull UUID eventId, @NotNull EventStatusEnum status);
}
