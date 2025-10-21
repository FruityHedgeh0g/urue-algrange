package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.SerieDto;
import fr.fruityhedgeh0g.repositories.SerieRepository;
import fr.fruityhedgeh0g.utilities.mappers.SerieMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class SerieService {
    @Inject
    SerieRepository serieRepository;

    @Inject
    SerieMapper serieMapper;

    public Try<List<SerieDto>> getAllSeries() {
        Log.info("Getting all series");
        return Try.of(() -> serieRepository
                .findAll()
                .stream()
                .map(serieMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all series", e));
    }

    public Try<SerieDto> getSerieById(@NotNull UUID serieId) {
        Log.info("Getting series with id: " + serieId);
        return Try.of(() -> serieRepository
                        .findByIdOptional(serieId)
                        .orElseThrow(NoSuchElementException::new))
                .map(serieMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Series not found: " + serieId);
                    }else {
                        Log.error("Error getting series with id: " + serieId, e);
                    }
                });
    }

    public Try<SerieDto> createSerie(@NotNull SerieDto serieDto) {
        Log.info("Creating series: " + serieDto.getSerieId());
        return null;
    }

    public Try<SerieDto> updateSerie(@NotNull SerieDto serieDto) {
        Log.info("Updating series: " + serieDto.getSerieId());
        return null;
    }

    public void deleteSerie(@NotNull UUID serieId) {
        Log.info("Deleting series with id: " + serieId);

    }
}
