package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureRequestDto;
import fr.fruityhedgeh0g.entities.FeatureRequestEntity;
import fr.fruityhedgeh0g.exceptions.ForbiddenActionException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.repositories.FeatureRequestRepository;
import fr.fruityhedgeh0g.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

/** Changes to the site asked by the Bureau, newest first. */
@ApplicationScoped
public class FeatureRequestService {

    @Inject FeatureRequestRepository requestRepository;
    @Inject UserRepository userRepository;

    public List<FeatureRequestDto> list() {
        return requestRepository.listNewestFirst().stream().map(FeatureRequestDto::of).toList();
    }

    @Transactional
    public FeatureRequestDto create(UUID askerId, FeatureRequestDto.Input input) {
        if (input.title() == null || input.title().isBlank() || input.description() == null || input.description().isBlank())
            throw new InvalidResourceException("A feature request has a title and a description.");
        FeatureRequestEntity request = new FeatureRequestEntity();
        request.setTitle(input.title().trim());
        request.setDescription(input.description().trim());
        request.setRequestedBy(userRepository.findByIdOptional(askerId)
                .orElseThrow(() -> new ForbiddenActionException("Unknown asker: " + askerId)));
        requestRepository.persist(request);
        return FeatureRequestDto.of(request);
    }
}
