package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import fr.fruityhedgeh0g.utilities.mappers.ConfigurationMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@ApplicationScoped
public class ConfigurationService {

    @Inject
    ConfigurationRepository configurationRepository;

    @Inject
    ConfigurationMapper configurationMapper;
}
