package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.SectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidInputException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestTransaction
public class SectorServiceTest {

    @InjectMock
    SectorRepository sectorRepository;

    @InjectMock
    GroupRepository groupRepository;

    @InjectMock
    GroupService groupService;

    @InjectMock
    SectorMapper sectorMapper;

    @Inject
    SectorService sectorService;

    SectorDto sectorDto;
    SectorEntity sectorEntity;
    GroupEntity groupEntity;

    @BeforeEach
    public void setUp() {
        reset(sectorRepository, groupService,groupRepository);

        UUID randomUUID = UUID.randomUUID();

        sectorDto = SectorDto.builder().sectorId(randomUUID).name("Mordor").build();
        sectorEntity = SectorEntity.builder().sectorId(randomUUID).name("Mordor").build();
        groupEntity = GroupEntity.builder().groupId(randomUUID).name("Legion of Sauron").build();

    }

    /** @see SectorService#getSectorById(UUID)  **/

    @Test
    public void getSectorById_Success(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));

        Assertions.assertEquals(
                sectorService.getSectorById(sectorDto.getSectorId()).get(),
                sectorDto
        );

    }

    @Test
    public void getSectorById_IdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.getSectorById(null)
                        .get()
        );
    }

    @Test
    public void getSectorById_UnknownSector_Failure(){
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.getSectorById(UUID.randomUUID())
                        .get()
        );
    }

    /** @see SectorService#createSector(SectorDto)  **/

    @Test
    public void createSector_Success(){
        when(sectorRepository.existsByName(any())).thenReturn(false);

        Assertions.assertEquals(
                sectorService.createSector(sectorDto).get(),
                sectorDto
        );

        Mockito.verify(sectorRepository).persist(any(SectorEntity.class));

    }

    @Test
    public void createSector_DuplicateSector_Failure(){
        when(sectorRepository.existsByName(any())).thenReturn(true);

        Assertions.assertThrowsExactly(
                DuplicateResourceException.class,
                () -> sectorService.createSector(sectorDto)
                        .get()
        );
    }

    @Test
    public void createSector_DtoIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.createSector(null)
        );
    }

    /** @see fr.fruityhedgeh0g.services.SectorService#updateSector(SectorDto) **/

    @Test
    public void updateSector_Success(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));

        Assertions.assertEquals(
                sectorService.updateSector(sectorDto).get(),
                sectorDto
        );

    }

    @Test
    public void updateSector_DtoIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.updateSector(null)
        );
    }

    @Test
    public void updateSector_UnknownSector_Failure(){
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.updateSector(sectorDto)
                        .get()
        );
    }

    /** @see fr.fruityhedgeh0g.services.SectorService#deleteSector(UUID) **/

    @Test
    public void deleteSector_Success(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));

        Assertions.assertDoesNotThrow(() -> sectorService.deleteSector(sectorDto.getSectorId()));

        Mockito.verify(sectorRepository).delete(any(SectorEntity.class));
    }

    @Test
    public void deleteSector_IdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.deleteSector(null)
        );
    }

    @Test
    public void deleteSector_UnknownSector_Failure(){
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.deleteSector(UUID.randomUUID())

        );
    }


    /** @see SectorService#assignGroupToSector(UUID, UUID) */

    @Test
    public void assignGroupToSector_Success(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));
        when(groupService.getInternalEntityById(any())).thenReturn(Try.of(() -> groupEntity));

        //Todo check l'insertion du secteur dans le group
        Assertions.assertEquals(
                sectorService.assignGroupToSector(sectorDto.getSectorId(),groupEntity.getGroupId()).get(),
                sectorDto
        );

    }

    @Test
    public void assignGroupToSector_UnknownGroup_Failure(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));
        when(groupRepository.findByIdOptional(any())).thenReturn(Optional.of(groupEntity));

        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.assignGroupToSector(UUID.randomUUID(), UUID.randomUUID())
                        .get()
        );
    }

    @Test
    public void assignGroupToSector_UnknownSector_Failure(){
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.assignGroupToSector(UUID.randomUUID(), UUID.randomUUID())
                .get()
        );
    }

    @Test
    public void assignGroupToSector_SectorIdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.assignGroupToSector(null, UUID.randomUUID())
                        .get()
        );
    }

    @Test
    public void assignGroupToSector_GroupIdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.assignGroupToSector(UUID.randomUUID(), null)
                        .get()
        );
    }

    @Test
    public void assignGroupToSector_GroupBelongsToAnotherSector_Failure(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));
        when(groupService.getInternalEntityById(any()).get()).thenReturn(groupEntity);

        Assertions.assertThrowsExactly(
                InvalidInputException.class,
                () -> sectorService.assignGroupToSector(sectorDto.getSectorId(),groupEntity.getGroupId())
                        .get()
        );
    }

    /** @see SectorService#unassignGroupFromSector(UUID, UUID) **/

    @Test
    public void unassignGroupToSector_Success(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));
        when(groupService.getInternalEntityById(groupEntity.getGroupId())).thenReturn(Try.of(() -> groupEntity));

        //verify(sectorEntity).removeGroup(any(GroupEntity.class));

        Assertions.assertEquals(
                sectorService.unassignGroupFromSector(sectorDto.getSectorId(),groupEntity.getGroupId()).get(),
                sectorDto
        );
    }

    @Test
    public void unassignGroupToSector_SectorIdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.unassignGroupFromSector(null, UUID.randomUUID())
                        .get()
        );

    }

    @Test
    public void unassignGroupToSector_GroupIdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> sectorService.unassignGroupFromSector(UUID.randomUUID(), null)
                        .get()
        );
    }

    @Test
    public void unassignGroupToSector_UnknownGroup_Failure(){
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.of(sectorEntity));

        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.unassignGroupFromSector(UUID.randomUUID(), UUID.randomUUID())
                        .get()
        );
    }

    @Test
    public void unassignGroupToSector_UnknownSector_Failure(){
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> sectorService.unassignGroupFromSector(UUID.randomUUID(), UUID.randomUUID())
                        .get()
        );
    }



}
