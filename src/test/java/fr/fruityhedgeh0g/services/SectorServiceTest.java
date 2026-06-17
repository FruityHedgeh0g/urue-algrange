package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

@QuarkusTest
@TestTransaction
public class SectorServiceTest {

    @InjectMock
    SectorRepository sectorRepository;

    @InjectMock
    GroupRepository groupRepository;

    @InjectMock
    GroupServiceImpl groupService;

    @Inject
    SectorServiceImpl sectorService;

    @BeforeEach
    public void setUp() {
        reset(sectorRepository, groupService,groupRepository);
    }

//    /** @see SectorServiceImpl#getAllSectors() **/
//
//    @Test
//    public void GetAllSectors_Success(){
//        SectorEntity sectorEntity = SectorEntity.builder().sectorId(UUID.randomUUID())
//                .name("FirstSector").build();
//
//        SectorEntity anotherSector = SectorEntity.builder().sectorId(UUID.randomUUID())
//                .name("SecondSector").build();
//
//        SectorDto sectorDto = SectorDto.builder().sectorId(sectorEntity.getSectorId())
//                .name(sectorEntity.getName()).build();
//
//        SectorDto anotherSectorDto = SectorDto.builder().sectorId(anotherSector.getSectorId())
//                .name(anotherSector.getName()).build();
//
//
//        List<SectorEntity> sectorEntities = List.of(sectorEntity, anotherSector);
//
//        List<SectorDto> sectorDtos = List.of(sectorDto, anotherSectorDto);
//
//        PanacheQuery<SectorEntity> mockedPanacheQuery = mock(PanacheQuery.class);
//        when(mockedPanacheQuery.page(any())).thenReturn(mockedPanacheQuery);
//        when(mockedPanacheQuery.stream()).thenReturn(sectorEntities.stream());
//        when(sectorRepository.findAll()).thenReturn(mockedPanacheQuery);
//
//        Assertions.assertEquals(sectorService.getAllSectors().get(),
//                sectorDtos
//        );
//    }
//
//    @Test
//    public void GetAllSectors_Failure_NotManagedException() {
//        when(sectorRepository.findAll()).thenThrow(new RuntimeException("Test exception"));
//
//        Assertions.assertThrows(RuntimeException.class, () -> {
//            sectorService.getAllSectors().get();
//        });
//    }
//
//    /** @see SectorServiceImpl#getSectorById(UUID) **/
//
//    @Test
//    public void GetSectorById_Success(){
//
//    }
//
//    @Test
//    public void GetSectorById_Failure_NotManagedException(){
//
//    }
//
//    @Test
//    public void GetSectorById_Failure_UnknownResource(){
//
//    }
//
//    @Test
//    public void GetSectorById_Failure_ConstraintViolation(){
//
//    }
//
//    /** @see SectorServiceImpl#createSector(SectorDto) **/
//
//    @Test
//    public void CreateSector_Success(){
//
//    }
//
//    @Test
//    public void CreateSector_Failure_ConstraintViolation(){
//
//    }
//
//    @Test
//    public void CreateSector_Failure_DuplicateResource(){
//
//    }
//
//    @Test
//    public void CreateSector_Failure_NotManagedException(){
//
//    }
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
//
//    /** @see SectorServiceImpl#updateSector(SectorDto) **/
//
//    @Test
//    public void UpdateSector_Success(){
//
//    }
//
//    @Test
//    public void UpdateSector_Failure_ConstraintViolation(){
//
//    }
//
//    @Test
//    public void UpdateSector_Failure_UnknownResource(){
//
//    }
//
//    @Test
//    public void UpdateSector_Failure_NotManagedException(){
//
//    }
//
//    /** @see SectorServiceImpl#deleteSector(UUID) **/
//
//    @Test
//    public void DeleteSector_Success(){
//
//    }
//
//    @Test
//    public void DeleteSector_Failure_ConstraintViolation(){
//
//    }
//
//    @Test
//    public void DeleteSector_Failure_UnknownResource(){
//
//    }
//
//    @Test
//    public void DeleteSector_Failure_NotManagedException(){
//
//    }





}
