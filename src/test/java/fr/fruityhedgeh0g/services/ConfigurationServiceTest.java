package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import fr.fruityhedgeh0g.utilities.mappers.ConfigurationMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@QuarkusTest
@TestTransaction
public class ConfigurationServiceTest {

    @InjectMock
    ConfigurationRepository configurationRepository;

    @Inject
    ConfigurationServiceImpl configurationService;

    ConfigurationEntity configurationEntity;
    ConfigurationDto configurationDto;


    @BeforeEach
    public void setUp() {
        reset(configurationRepository);

        configurationEntity = ConfigurationEntity.builder()
                .name("firstConfiguration")
                .value("first value")
                .build();

        configurationDto = ConfigurationDto.builder()
                .name(configurationEntity.getName())
                .value(configurationEntity.getValue())
                .build();
    }

    /** @see ConfigurationServiceImpl#getAllConfigurations()  **/

    @Test
    public void getAllConfigurations_Success(){
    }
    
    @Test 
    public void getAllConfigurations_Failure_NotManagedException(){
        when(configurationRepository.findAll()).thenThrow(new RuntimeException("Dummy exception"));
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> configurationService.getAllConfigurations().get()
        );
    }

    /** @see ConfigurationServiceImpl#getConfigurationByName(String)  **/

    @Test
    public void getConfigurationByName_Success(){
        when(configurationRepository.findByIdOptional(any())).thenReturn(Optional.of(configurationEntity));

        Assertions.assertEquals(configurationService.getConfigurationByName("firstConfiguration").get(),this.configurationDto);

    }

    @Test
    public void getConfigurationByName_Failure_UnknownResourceException(){
        Assertions.assertThrowsExactly(UnknownResourceException.class,
                () -> configurationService.getConfigurationByName("DummyName").get()
        );
    }

    @Test
    public void getConfigurationByName_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> configurationService.getConfigurationByName(null)
        );
    }

    @Test
    public void getConfigurationByName_Failure_NotManagedException(){
        when(configurationRepository.findByIdOptional(any())).thenThrow(new RuntimeException("Dummy exception"));

        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> configurationService.getConfigurationByName("DummyName").get()
        );
    }


    /** @see ConfigurationServiceImpl#updateConfiguration(ConfigurationDto)  **/

    @Test
    public void updateConfiguration_Success(){
        when(configurationRepository.findByIdOptional(any())).thenReturn(Optional.of(configurationEntity));

        Assertions.assertEquals(configurationService.updateConfiguration(configurationDto).get(),this.configurationDto);
    }

    @Test
    public void updateConfiguration_Failure_UnknownResourceException(){
        Assertions.assertThrowsExactly(UnknownResourceException.class,
                () -> configurationService.getConfigurationByName("DummyName").get()
        );
    }

    @Test
    public void updateConfiguration_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> configurationService.updateConfiguration(null)
        );
    }

    @Test
    public void updateConfiguration_Failure_NotManagedException(){
        when(configurationRepository.findByIdOptional(any())).thenThrow(new RuntimeException("Dummy exception"));

        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> configurationService.getConfigurationByName("DummyName").get()
        );
    }

}
