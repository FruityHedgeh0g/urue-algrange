package fr.fruityhedgeh0g.events.listeners;

import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class KeycloakUserEventListener {

    @Inject
    @Identifier("serviceImpl")
    UserService userService;
}
