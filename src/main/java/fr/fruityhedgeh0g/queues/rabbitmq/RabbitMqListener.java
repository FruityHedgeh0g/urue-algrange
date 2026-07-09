package fr.fruityhedgeh0g.queues.rabbitmq;

import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import jakarta.inject.Inject;

public class RabbitMqListener {

    @Inject
    InternalUserService internalUserService;

    private RabbitMqConnector rabbitMqConnector;

    public RabbitMqListener(RabbitMqConnector rabbitMqConnector) {
        this.rabbitMqConnector = rabbitMqConnector;
    }


    private void userUpdatedEvent(){

    }

    private void userDeletedEvent(){

    }

    private void userCreatedEvent(){

    }

}
