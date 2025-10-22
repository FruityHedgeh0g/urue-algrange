package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.services.SeriesService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

public class SeriesController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    SeriesService seriesService;
}
