package fr.fruityhedgeh0g.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import jakarta.inject.Inject;

public class RabbitMqPublisher {

    @Inject
    InternalUserService internalUserService;

    private Channel channel;

    private RabbitConnectorImpl rabbitMqConnector;

    public RabbitMqPublisher(RabbitConnectorImpl rabbitMqConnector) {
        this.rabbitMqConnector = rabbitMqConnector;
    }


    private void initChannel() throws Exception {

    }


    private void userUpdatedEvent(){

    }

    private void userDeletedEvent(){

    }

    private void userCreatedEvent(){

    }

}
