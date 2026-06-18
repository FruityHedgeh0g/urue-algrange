package fr.fruityhedgeh0g.events.listeners;

import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.RabbitMqConnector;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

@ApplicationScoped
public class KeycloakUserEventListener {

//    private final RabbitMqConnector rabbitMqConnector;
//    private final UserService userService;
//
//    KeycloakUserEventListener(
//            RabbitMqConnector rabbitMqConnector,
//            @Identifier("serviceImpl") UserService userService
//    ) {
//        this.rabbitMqConnector = rabbitMqConnector;
//        this.userService = userService;
//    }


}
