package fr.fruityhedgeh0g.services.decorators.logs;

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

@Priority(200)
@Decorator
public class FeatureLogDecorator implements FeatureService{

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
    public FeatureDto getByName(String name) {
        Log.debugf("Retrieving feature by name %s...",name);
        return Try.of(() -> featureService.getByName(name))
                .onSuccess(feature -> {
                    Log.debugf("Feature retrieved: "+feature.toString());
                })
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex, "Feature not found: %s", name);
                        default -> Log.errorf(t,"An error occurred while retrieving feature.");
                    }
                })
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
