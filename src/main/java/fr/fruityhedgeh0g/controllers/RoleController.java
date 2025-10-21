package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.services.RoleService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

public class RoleController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    RoleService roleService;
}
