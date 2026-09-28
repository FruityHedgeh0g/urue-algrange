package fr.fruityhedgeh0g.utilities.logging;

import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/** Les appels passent par l'intercepteur de journalisation sans altérer résultats ni exceptions. */
@QuarkusTest
class LoggingInterceptorTest {

    @Inject
    SectorService sectorService;

    @InjectMock
    SectorRepository sectorRepository;

    @Test
    void returnsTheServiceResult() {
        when(sectorRepository.listAll()).thenReturn(List.of());
        assertEquals(List.of(), sectorService.listAll());

        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.empty());
        assertTrue(sectorService.getById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void propagatesTheServiceException() {
        when(sectorRepository.findByIdOptional(any())).thenReturn(Optional.empty());
        assertThrows(UnknownResourceException.class, () -> sectorService.assignGroup(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void describesResults() {
        assertEquals(" with 2 element(s)", LoggingInterceptor.describe(List.of(1, 2)));
        assertEquals(" with no result", LoggingInterceptor.describe(Optional.empty()));
        assertEquals("", LoggingInterceptor.describe(null));
    }
}
