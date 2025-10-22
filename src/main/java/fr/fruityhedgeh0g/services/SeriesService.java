package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
import fr.fruityhedgeh0g.model.dtos.SeriesDto;
import fr.fruityhedgeh0g.repositories.SeriesRepository;
import fr.fruityhedgeh0g.utilities.mappers.SeriesMapper;
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
public class SeriesService {
    @Inject
    SeriesRepository seriesRepository;

    @Inject
    SeriesMapper seriesMapper;

    public Try<List<SeriesDto>> getAllSeries() {
        Log.info("Getting all series");
        return Try.of(() -> seriesRepository
                .findAll()
                .stream()
                .map(seriesMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all series", e));
    }

    public Try<SeriesDto> getSeriesById(@NotNull UUID seriesId) {
        Log.info("Getting series with id: " + seriesId);
        return Try.of(() -> seriesRepository
                        .findByIdOptional(seriesId)
                        .orElseThrow(NoSuchElementException::new))
                .map(seriesMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Series not found: " + seriesId);
                    }else {
                        Log.error("Error getting series with id: " + seriesId, e);
                    }
                });
    }

    public Try<SeriesDto> createSeries(@NotNull SeriesDto seriesDto) {
        Log.info("Creating series: " + seriesDto.getSerieId());
        return null;
    }

    public Try<SeriesDto> updateSeries(@NotNull SeriesDto seriesDto) {
        Log.info("Updating series: " + seriesDto.getSerieId());
        return null;
    }

    public void deleteSeries(@NotNull UUID seriesId) {
        Log.info("Deleting series with id: " + seriesId);
        Try.run(() -> seriesRepository.deleteById(seriesId))
                .onFailure(e -> Log.error("Error deleting series with id: " + seriesId, e));

    }
}
