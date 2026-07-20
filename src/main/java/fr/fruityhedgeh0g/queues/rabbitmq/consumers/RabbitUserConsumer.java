package fr.fruityhedgeh0g.queues.rabbitmq.consumers;

import com.rabbitmq.client.*;
import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.exceptions.UnknownQueueRoutingKeyException;
import fr.fruityhedgeh0g.queues.rabbitmq.KeycloakDeserializer;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RabbitUserConsumer extends DefaultConsumer {
    private final String userDeletionRoute;
    private final String userUpdateRoute;
    private final String userCreationRoute;
    private final InternalUserService internalUserService;
    private final KeycloakDeserializer deserializer;

    public RabbitUserConsumer(
            Channel channel,
            InternalUserService internalUserService,
            KeycloakDeserializer deserializer,
            String userDeletionRoute,
            String userUpdateRoute,
            String userCreationRoute
    ) {
        super(channel);
        this.internalUserService = internalUserService;
        this.userDeletionRoute = userDeletionRoute;
        this.userUpdateRoute = userUpdateRoute;
        this.userCreationRoute = userCreationRoute;
        this.deserializer = deserializer;
    }

    @Override
    public void handleDelivery(
            String consumerTag,
            Envelope envelope,
            AMQP.BasicProperties properties,
            byte[] body
    ) throws IOException {
        String routingKey = envelope.getRoutingKey();
        String contentType = properties.getContentType();
        long deliveryTag = envelope.getDeliveryTag();
        String content = new String(body, StandardCharsets.UTF_8);

        try {
            dispatch(routingKey, content);
            getChannel().basicAck(deliveryTag, false);
        }catch (Exception e){
            //todo : définir une DLX pour éviter les pertes
            getChannel().basicNack(deliveryTag, false, false);
        }
    }

    private void dispatch(String routingKey, String content ) {
        if (routingKey.equals(userDeletionRoute)){
            handleUserDeletion();
        } else if (routingKey.equals(userUpdateRoute)){
            handleUserUpdate(content);
        } else if (routingKey.equals(userCreationRoute)){
            handleUserCreation(content);
        }else {
            throw new UnknownQueueRoutingKeyException("Unknown routing key");
        }
    }

    private void handleUserDeletion(){

    }

    private void handleUserCreation(String content) {
        UserDto userDto = deserializer.deserializePayloadToUserDto(content);
        internalUserService.doCreate(userDto);
    }

    private void handleUserUpdate(String content){
        UserDto userDto = deserializer.deserializePayloadToUserDto(content);
        internalUserService.doUpdate(userDto);
    }
}
