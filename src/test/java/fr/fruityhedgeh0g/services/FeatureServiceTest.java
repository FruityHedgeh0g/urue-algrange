package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestTransaction
public class FeatureServiceTest {

    @InjectMock
    FeatureRepository featureRepository;

    @Inject
    FeatureServiceImpl featureService;

    FeatureEntity featureEntity;
    FeatureDto featureDto;

    FeatureEntity anotherFeatureEntity;
    FeatureDto anotherFeatureDto;

    List<FeatureEntity> featureEntities;
    List<FeatureDto> featureDtos;

    @BeforeEach
    public void setUp() {
        reset(featureRepository);

        featureEntity = FeatureEntity.builder()
                .name("firstFeature")
                .isActive(true)
                .description("First feature")
                .build();

        featureDto = FeatureDto.builder()
                .name(featureEntity.getName())
                .isActive(featureEntity.getIsActive())
                .description(featureEntity.getDescription())
                .build();

        anotherFeatureEntity = FeatureEntity.builder()
                .name("secondFeature")
                .isActive(false)
                .description("Second feature")
                .build();

        anotherFeatureDto = FeatureDto.builder()
                .name(anotherFeatureEntity.getName())
                .isActive(anotherFeatureEntity.getIsActive())
                .description(anotherFeatureEntity.getDescription())
                .build();

        featureEntities = List.of(featureEntity, anotherFeatureEntity);
        featureDtos = List.of(featureDto, anotherFeatureDto);

    }

    /** @see FeatureServiceImpl#getAllFeatures()  **/

    @Test
    public void getAllFeatures_Success(){
        PanacheQuery<FeatureEntity> mockedPanacheQuery = mock(PanacheQuery.class);
        when(mockedPanacheQuery.page(any())).thenReturn(mockedPanacheQuery);
        when(mockedPanacheQuery.stream()).thenReturn(featureEntities.stream());
        when(featureRepository.findAll()).thenReturn(mockedPanacheQuery);

        Assertions.assertEquals(featureService.getAllFeatures().get(),featureDtos);
    }

    @Test
    public void getAllFeatures_Failure_NotManagedException(){
        when(featureRepository.findAll()).thenThrow(new RuntimeException("Dummy exception"));
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> featureService.getAllFeatures().get()
        );
    }

    /** @see FeatureServiceImpl#getFeatureByName(String)  **/

    @Test
    public void getFeatureByName_Success(){
        when(featureRepository.findByIdOptional(any())).thenReturn(Optional.of(featureEntity));

        Assertions.assertEquals(featureService.getFeatureByName("firstFeature").get(),featureDto);

    }

    @Test
    public void getFeatureByName_Failure_UnknownResourceException(){
        Assertions.assertThrowsExactly(UnknownResourceException.class,
                () -> featureService.getFeatureByName("DummyName").get()
        );
    }

    @Test
    public void getFeatureByName_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> featureService.getFeatureByName(null)
        );
    }

    @Test
    public void getFeatureByName_Failure_NotManagedException(){
        when(featureRepository.findByIdOptional(any())).thenThrow(new RuntimeException("Dummy exception"));

        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> featureService.getFeatureByName("DummyName").get()
        );
    }


    /** @see FeatureServiceImpl#updateFeature(FeatureDto)  **/

    @Test
    public void updateFeature_Success(){
        when(featureRepository.findByIdOptional(any())).thenReturn(Optional.of(featureEntity));

        Assertions.assertEquals(featureService.updateFeature(featureDto).get(),featureDto);
    }

    @Test
    public void updateFeature_Failure_UnknownResourceException(){
        Assertions.assertThrowsExactly(UnknownResourceException.class,
                () -> featureService.updateFeature(featureDto).get()
        );
    }

    @Test
    public void updateFeature_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> featureService.updateFeature(null)
        );
    }

    @Test
    public void updateFeature_Failure_NotManagedException(){
        when(featureRepository.findByIdOptional(any())).thenThrow(new RuntimeException("Dummy exception"));

        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> featureService.updateFeature(featureDto).get()
        );
    }

}
