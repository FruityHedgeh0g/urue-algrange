package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
@TestTransaction
public class EventServiceTest {

    @BeforeEach
    public void setUp() {

    }

    /** @see EventServiceImpl#getAllEvents() **/

    @Test
    public void GetAllEvents_Success(){

    }

    @Test
    public void GetAllEvents_Failure_NotManagedException(){

    }


    /** @see EventServiceImpl#getAllEventsFiltered(String) **/

    @Test
    public void GetAllEventsFiltered_Success(){

    }

    @Test
    public void GetAllEventsFiltered_Failure_NotManagedException(){

    }



    /** @see EventServiceImpl#getEventById(UUID)  **/

    @Test
    public void GetEventById_Success(){

    }

    @Test
    public void GetEventById_Failure_NotManagedException(){

    }

    @Test
    public void GetEventById_Failure_UnknownResource(){

    }

    @Test
    public void GetEventById_Failure_ConstraintViolation(){

    }

    /** @see EventServiceImpl#createEvent(EventDto)  **/

    @Test
    public void CreateEvent_Success(){

    }

    @Test
    public void CreateEvent_Failure_ConstraintViolation(){

    }

    @Test
    public void CreateEvent_Failure_NotManagedException(){

    }

    @Test
    public void CreateEvent_Failure_DuplicateResource(){

    }


    /** @see EventServiceImpl#updateEvent(EventDto) **/

    @Test
    public void UpdateEvent_Success(){

    }

    @Test
    public void UpdateEvent_Failure_ConstraintViolation(){

    }

    @Test
    public void UpdateEvent_Failure_UnknownResource(){

    }

    @Test
    public void UpdateEvent_Failure_NotManagedException(){

    }

    @Test
    public void UpdateEvent_Failure_DuplicateResource(){

    }


    /** @see EventServiceImpl#deleteEvent(UUID) **/

    @Test
    public void DeleteEvent_Success(){

    }

    @Test
    public void DeleteEvent_Failure_ConstraintViolation(){

    }

    @Test
    public void DeleteEvent_Failure_UnknownResource(){

    }

    @Test
    public void DeleteEvent_Failure_NotManagedException(){

    }

    /** @see EventServiceImpl#addParticipant(UUID, UUID) **/

    @Test
    public void AddParticipant_Success(){

    }

    @Test
    public void AddParticipant_Failure_ConstraintViolation_UserIdIsNull(){

    }

    @Test
    public void AddParticipant_Failure_ConstraintViolation_EventIdIsNull(){

    }

    @Test
    public void AddParticipant_Failure_NotManagedException(){

    }

    @Test
    public void AddParticipant_Failure_UnknownResource_UserId(){

    }

    @Test
    public void AddParticipant_Failure_UnknownResource_EventId(){

    }

    @Test
    public void AddParticipant_Failure_DuplicateResource_UserAlreadyAssigned(){

    }



    /** @see EventServiceImpl#removeParticipant(UUID, UUID) **/

    @Test
    public void RemoveParticipant_Success(){

    }

    @Test
    public void RemoveParticipant_Failure_ConstraintViolation_UserIdIsNull(){

    }

    @Test
    public void RemoveParticipant_Failure_ConstraintViolation_EventIdIsNull(){

    }

    @Test
    public void RemoveParticipant_Failure_NotManagedException(){

    }

    @Test
    public void RemoveParticipant_Failure_UnknownResource_UserId(){

    }

    @Test
    public void RemoveParticipant_Failure_UnknownResource_EventId(){

    }



    /** @see EventServiceImpl#addOrganizer(UUID, UUID) **/

    @Test
    public void AddOrganizer_Success(){

    }

    @Test
    public void AddOrganizer_Failure_ConstraintViolation_UserIdIsNull(){

    }

    @Test
    public void AddOrganizer_Failure_ConstraintViolation_EventIdIsNull(){

    }

    @Test
    public void AddOrganizer_Failure_NotManagedException(){

    }

    @Test
    public void AddOrganizer_Failure_UnknownResource_UserId(){

    }

    @Test
    public void AddOrganizer_Failure_UnknownResource_EventId(){

    }

    @Test
    public void AddOrganizer_Failure_DuplicateResource_UserAlreadyAssigned(){

    }

    /** @see EventServiceImpl#removeOrganizer(UUID, UUID) **/

    @Test
    public void RemoveOrganizer_Success(){

    }

    @Test
    public void RemoveOrganizer_Failure_ConstraintViolation_UserIdIsNull(){

    }

    @Test
    public void RemoveOrganizer_Failure_ConstraintViolation_EventIdIsNull(){

    }

    @Test
    public void RemoveOrganizer_Failure_NotManagedException(){

    }

    @Test
    public void RemoveOrganizer_Failure_UnknownResource_UserId(){

    }

    @Test
    public void RemoveOrganizer_Failure_UnknownResource_EventId(){

    }



}
