package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.services.interfaces.internals.InternalEventService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicEventService;

public interface EventService extends PublicEventService, InternalEventService {
}
