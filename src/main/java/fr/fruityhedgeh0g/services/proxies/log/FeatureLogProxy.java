package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Priority(200)
@Decorator
public class FeatureLogProxy implements FeatureService{

    @Inject
    @Delegate
    FeatureService featureService;

    @Override
    public List<FeatureDto> listAll() {
        Log.debugf("Retrieving all features...");
        return Try.of(featureService::listAll)
                .onSuccess(features -> Log.debugf("%d features retrieved.",features.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving features."))
                .get();
    }

    @Override
    public Optional<FeatureDto> getByName(String name) {
        Log.debugf("Retrieving feature by name %s...",name);
        return Try.of(() -> featureService.getByName(name))
                .onSuccess(feature -> {
                    if (feature.isPresent())
                        Log.debugf("Feature retrieved: "+feature.toString());
                    else Log.debugf("Feature with name %s not found.",name);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving feature."))
                .get();
    }

    @Override
    public FeatureDto update(FeatureDto featureDto) {
        Log.debugf("Updating an existing feature: %s", featureDto.toString());
        return Try.of(() -> featureService.update(featureDto))
                .onSuccess(feature -> Log.debugf("Feature updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Feature %s not found.", featureDto.getName());
                        default -> Log.errorf(t,"An error occurred while updating feature.");
                    }
                })
                .get();
    }
}
