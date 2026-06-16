package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import fr.fruityhedgeh0g.utilities.mappers.FeatureMapper;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class FeatureServiceImpl implements FeatureService {
    @Inject
    FeatureMapper featureMapper;

    @Inject
    FeatureRepository featureRepository;

    @Override
    public List<FeatureDto> listAll() {
        return featureRepository.listAll()
                .stream()
                .map(featureMapper::toDto)
                .toList();
    }

    @Override
    public Optional<FeatureDto> getByName(String name) {
        return null;
    }

    @Override
    public FeatureDto update(FeatureDto featureDto) {
        return null;
    }

//    @Override
//    @Transactional
//    public Try<FeatureDto> getFeatureByName( String name) {
//        Log.infof("Getting feature by name: %s", name);
//        return Try.of(() -> featureRepository.findByIdOptional(name).orElseThrow(
//                () -> new UnknownResourceException("Feature not found: " + name)))
//        .map(featureMapper::toDto)
//        .onFailure(e -> {
//            if (e instanceof UnknownResourceException ex) {
//                Log.warn(ex.getMessage());
//            } else {
//                Log.errorf(e,"Error getting feature by name: %s", name );
//            }
//        });
//    }
//
//    @Override
//    @Transactional
//    public Try<List<FeatureDto>> getAllFeatures() {
//        Log.info("Getting all features");
//        return Try.of(() -> featureRepository
//                .findAll()
//                .stream()
//                .map(featureMapper::toDto)
//                .toList())
//                .onFailure(e -> {
//                    Log.errorf(e,"Error getting all features");
//                });
//    }
//
//    public Try<FeatureDto> updateFeature( FeatureDto dto) {
//        Log.infof("Updating feature: %s", dto.getName());
//        return Try.of(() -> {
//            Log.infof("Updating feature: %s", dto.getName());
//            FeatureEntity feature = featureRepository.findByIdOptional(dto.getName())
//                    .orElseThrow(() -> new UnknownResourceException("Feature not found: " + dto.getName()));
//
//            featureMapper.partialDtoToEntity(feature, dto);
//            return featureMapper.toDto(feature);
//        });
//    }
}
