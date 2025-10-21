package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.services.SerieService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

public class SerieController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    SerieService serieService;
}
