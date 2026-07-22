package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@QuarkusTest
public class SectorServiceTest {

    @Inject
    SectorServiceImpl sectorService;

    @Inject
    SectorRepository sectorRepository;

    @Inject
    GroupRepository groupRepository;

    @Inject
    SectorMapper sectorMapper;

    @BeforeEach
    @TestTransaction
    public void setUp() {
        sectorRepository.deleteAll();
    }

    /** @see SectorServiceImpl#listAll() **/

    //Ne marche pas car nécéssite un override de equals et hashCode
    @Test
    @TestTransaction
    public void listAllSectors_Success(){
        SectorEntity firstSector = SectorEntity.builder().name("First sector").build();
        sectorRepository.persist(firstSector);

        SectorEntity secondSector = SectorEntity.builder().name("Second sector").build();
        sectorRepository.persist(secondSector);

        List<SectorEntity> comparativeSectors = List.of(firstSector,secondSector);
        List<SectorEntity> gatheredSectors = sectorService.listAll().stream().map(sectorMapper::toEntity).toList();

        Assertions.assertEquals(comparativeSectors.size(), gatheredSectors.size());
        Assertions.assertIterableEquals(comparativeSectors, gatheredSectors);
    }

    @Test
    @TestTransaction
    public void getAllSectors_NotFound(){
        List<SectorDto> sectors = new ArrayList<>();
        List<SectorDto> gatheredSectors = sectorService.listAll();
        Assertions.assertEquals(0,gatheredSectors.size());
        Assertions.assertEquals(sectors, gatheredSectors);
    }

    /** @see SectorServiceImpl#getById(UUID) () **/

    //Ne marche pas car nécéssite un override de equals et hashCode
    @Test
    @TestTransaction
    public void getById_Success(){
        SectorEntity firstSector = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(firstSector);

        SectorEntity retrievedSector = sectorMapper.toEntity(sectorService.getById(firstSector.getSectorId()));
        Assertions.assertEquals(firstSector, retrievedSector);

    }

    @Test
    @TestTransaction
    public void getById_NotFound(){
        Assertions.assertThrows(UnknownResourceException.class, () -> sectorService.getById(UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void getById_NullId(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> sectorService.getById(null));
    }

    /** @see SectorServiceImpl#create(SectorDto) () **/

    @Test
    @TestTransaction
    public void create_NullDto(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> sectorService.create(null));
    }

    @Test
    @TestTransaction
    public void create_Success(){
        SectorDto sectorDto = sectorService.create(
                SectorDto.builder().name("Test Sector").build()
        );

        SectorDto retrievedSector = sectorService.getById(sectorDto.getSectorId());
        Assertions.assertEquals(sectorDto,retrievedSector);
    }

    @Test
    @TestTransaction
    public void create_Duplicate(){
        SectorEntity sector = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sector);

        Assertions.assertTrue(sectorRepository.existsByName(sector.getName()));

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> sectorService.create(sectorMapper.toDto(sector))
        );

    }

    /** @see SectorServiceImpl#update(SectorDto) () **/

    @Test
    @TestTransaction
    public void update_Success(){
        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        Assertions.assertTrue(sectorRepository.existsByName(sectorEntity.getName()));

        SectorDto updatedSectorDto = SectorDto.builder()
                .name("Updated Sector")
                .sectorId(sectorEntity.getSectorId())
                .build();

        Assertions.assertDoesNotThrow(() -> sectorService.update(updatedSectorDto));

        Assertions.assertTrue(sectorRepository.existsByName(sectorEntity.getName()));

    }

    @Test
    @TestTransaction
    public void update_NotFound(){
        SectorDto sectorDto = SectorDto
                .builder()
                .name("Test Sector")
                .sectorId(UUID.randomUUID())
                .build();

        Assertions.assertThrows(UnknownResourceException.class, () -> sectorService.update(sectorDto));

    }

    @Test
    @TestTransaction
    public void update_NullDto(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> sectorService.update(null));
    }

    @Test
    @TestTransaction
    public void update_Duplicate(){
        sectorRepository.persist(SectorEntity.builder().name("Test Sector").build());

        SectorEntity sectorEntity = SectorEntity.builder().name("To be renamed Sector").build();
        sectorRepository.persist(sectorEntity);

        Assertions.assertTrue(sectorRepository.existsByName(sectorEntity.getName()));

        SectorDto updatedSectorDto = SectorDto.builder()
                .name("Test Sector")
                .sectorId(sectorEntity.getSectorId())
                .build();

        Assertions.assertThrows(
                DuplicateResourceException.class,
                () -> sectorService.update(updatedSectorDto)
        );
    }

    /** @see SectorServiceImpl#delete(UUID) () **/

    @Test
    @TestTransaction
    public void delete_NullId(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> sectorService.delete(null));
    }

    @Test
    @TestTransaction
    public void delete_NotFound(){
        Assertions.assertThrows(UnknownResourceException.class, () -> sectorService.delete(UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void delete_GroupAssigned(){
        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        sectorEntity.addGroup(groupEntity);

        Assertions.assertThrows(InvalidResourceException.class, () -> sectorService.delete(sectorEntity.getSectorId()));
    }

    @Test
    @TestTransaction
    public void delete_Success(){

        SectorEntity sectorEntity = SectorEntity.builder().name("Test sector").build();
        sectorRepository.persist(sectorEntity);

        Assertions.assertDoesNotThrow(() -> sectorService.delete(sectorEntity.getSectorId()));
        Assertions.assertThrows(UnknownResourceException.class, () -> sectorService.getById(sectorEntity.getSectorId()));
    }

    /** @see SectorServiceImpl#assignGroup(UUID, UUID) () **/

    @Test
    @TestTransaction
    public void assignGroup_NullSectorId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sectorService.assignGroup(null, UUID.randomUUID())
        );
    }

    @Test
    @TestTransaction
    public void assignGroup_NullGroupId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sectorService.assignGroup(UUID.randomUUID(), null)
        );
    }

    @Test
    @TestTransaction
    public void assignGroup_GroupNotFound(){
        Assertions.assertThrows(
                UnknownResourceException.class,
                () -> sectorService.assignGroup(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void assignGroup_SectorNotFound(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertThrows(UnknownResourceException.class,
                () -> sectorService.assignGroup(UUID.randomUUID() , groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void assignGroup_GroupAlreadyAssigned(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        SectorEntity anotherSectorEntity = SectorEntity.builder().name("Another Sector").build();
        sectorRepository.persist(anotherSectorEntity);

        anotherSectorEntity.addGroup(groupEntity);

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> sectorService.assignGroup(sectorEntity.getSectorId(), groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void assignGroup_Success(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        Assertions.assertDoesNotThrow(() -> sectorService.assignGroup(sectorEntity.getSectorId(), groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void assignGroup_AlreadyPerformed(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        sectorEntity.addGroup(groupEntity);

        Assertions.assertDoesNotThrow(() -> sectorService.assignGroup(sectorEntity.getSectorId(), groupEntity.getGroupId()));
    }

    /** @see SectorServiceImpl#unassignGroup(UUID, UUID) () **/

    @Test
    @TestTransaction
    public void unassignGroup_Success(){
        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        sectorEntity.addGroup(groupEntity);

        Assertions.assertDoesNotThrow(() -> sectorService.unassignGroup(sectorEntity.getSectorId(), groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void unassignGroup_AlreadyPerformed(){
        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertDoesNotThrow(() -> sectorService.unassignGroup(sectorEntity.getSectorId(), groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void unassignGroup_GroupNotFound(){
        Assertions.assertThrows(
                UnknownResourceException.class,
                () -> sectorService.unassignGroup(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void unassignGroup_SectorNotFound(){

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertThrows(UnknownResourceException.class,
                () -> sectorService.unassignGroup(UUID.randomUUID(), groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void unassignGroup_NullSectorId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sectorService.unassignGroup(null, UUID.randomUUID())
        );
    }

    @Test
    @TestTransaction
    public void unassignGroup_NullGroupId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sectorService.unassignGroup(UUID.randomUUID(),null)
        );
    }

    @Test
    @TestTransaction
    public void unassignGroup_GroupAssignedToAnotherSector(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        SectorEntity sectorEntity = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(sectorEntity);

        SectorEntity anotherSectorEntity = SectorEntity.builder().name("Another Sector").build();
        sectorRepository.persist(anotherSectorEntity);

        anotherSectorEntity.addGroup(groupEntity);

        Assertions.assertThrows(InvalidResourceException.class,
                () -> sectorService.unassignGroup(sectorEntity.getSectorId(), groupEntity.getGroupId()));
    }

    /** @see SectorServiceImpl#getByUserId(UUID) () **/

}
