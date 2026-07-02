package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@QuarkusTest
public class SectorServiceTest {

    @Inject
    SectorServiceImpl sectorService;

    @Inject
    SectorRepository sectorRepository;

    @Inject
    SectorMapper sectorMapper;

    @BeforeEach
    public void setUp() {
        
    }

    /** @see SectorServiceImpl#listAll() **/

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
        Assertions.assertEquals(gatheredSectors.size(), 0);
        Assertions.assertEquals(sectors, gatheredSectors);
    }

    /** @see SectorServiceImpl#getById(UUID) () **/

    @Test
    @TestTransaction
    public void getById_Success(){
        SectorEntity firstSector = SectorEntity.builder().name("Test Sector").build();
        sectorRepository.persist(firstSector);

        SectorDto retrievedSector = sectorService.getById(firstSector.getSectorId()).orElseThrow();
        Assertions.assertEquals(firstSector, sectorMapper.toEntity(retrievedSector));

    }

    @Test
    @TestTransaction
    public void getById_NotFound(){
        Assertions.assertFalse(sectorService.getById(UUID.randomUUID()).isPresent());
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

    }

    @Test
    @TestTransaction
    public void create_Duplicate(){

    }

    /** @see SectorServiceImpl#update(SectorDto) () **/

    @Test
    @TestTransaction
    public void update_Success(){

    }

    @Test
    @TestTransaction
    public void update_NotFound(){

    }

    @Test
    @TestTransaction
    public void update_NullDto(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> sectorService.update(null));
    }

    @Test
    @TestTransaction
    public void update_Duplicate(){

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
        //Assertions.assertThrows(InvalidResourceException.class, () -> sectorService.delete(UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void delete_Success(){

        SectorEntity sectorEntity = SectorEntity.builder().name("Test sector").build();
        sectorRepository.persist(sectorEntity);

        Assertions.assertDoesNotThrow(() -> sectorService.delete(sectorEntity.getSectorId()));
        Assertions.assertFalse(sectorService.getById(sectorEntity.getSectorId()).isPresent());
    }

    /** @see SectorServiceImpl#assignGroup(UUID, UUID) () **/

    /** @see SectorServiceImpl#unassignGroup(UUID, UUID) () **/
//
//    /** @see SectorServiceImpl#assignGroupToSector(UUID, UUID) **/
//
//    @Test
//    public void AssignGroupToSector_Success(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_ConstraintViolation_GroupIdIsNull(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_ConstraintViolation_SectorIdIsNull(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_UnknownResource_GroupId(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_UnknownResource_SectorId(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_NotManagedException(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_DuplicateResource_GroupAlreadyAssigned(){
//
//    }
//
//    @Test
//    public void AssignGroupToSector_Failure_DuplicateResource_GroupAlreadyAssignedToAnotherSector(){
//
//    }
//
//    /** @see SectorServiceImpl#unassignGroupFromSector(UUID, UUID) **/
//
//    @Test
//    public void UnassignGroupFromSector_Success(){
//
//    }
//
//    @Test
//    public void UnassignGroupFromSector_Failure_ConstraintViolation_GroupIdIsNull(){
//
//    }
//
//    @Test
//    public void UnassignGroupFromSector_Failure_ConstraintViolation_SectorIdIsNull(){
//
//    }
//
//    @Test
//    public void UnassignGroupFromSector_Failure_UnknownResource_GroupId(){
//
//    }
//
//    @Test
//    public void UnassignGroupFromSector_Failure_UnknownResource_SectorId(){
//
//    }
//
//    @Test
//    public void UnassignGroupFromSector_Failure_NotManagedException(){
//
//    }
}
