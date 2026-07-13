package fr.fruityhedgeh0g.queues.rabbitmq.consumers;

import com.rabbitmq.client.*;
import fr.fruityhedgeh0g.exceptions.UnknownQueueRoutingKeyException;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RabbitUserConsumer extends DefaultConsumer {
    private final String userDeletionRoute;
    private final String userUpdateRoute;
    private final String userCreationRoute;
    private final InternalUserService internalUserService;

    public RabbitUserConsumer(
            Channel channel,
            InternalUserService internalUserService,
            String userDeletionRoute,
            String userUpdateRoute,
            String userCreationRoute
    ) {
        super(channel);
        this.internalUserService = internalUserService;
        this.userDeletionRoute = userDeletionRoute;
        this.userUpdateRoute = userUpdateRoute;
        this.userCreationRoute = userCreationRoute;
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
        } catch (UnknownQueueRoutingKeyException e) {
            getChannel().basicNack(deliveryTag, false, false);
        }catch (Exception e){
            getChannel().basicNack(deliveryTag, false, true);
        }
    }

    private void dispatch(String routingKey, String content ) throws UnknownQueueRoutingKeyException {
        if (routingKey.equals(userDeletionRoute)){
            handleUserDeletion();
        } else if (routingKey.equals(userUpdateRoute)){
            handleUserUpdate();
        } else if (routingKey.equals(userCreationRoute)){
            handleUserCreation();
        }else {
            throw new UnknownQueueRoutingKeyException("Unknown routing key");
        }
    }

    private void handleUserDeletion(){

    }

    private void handleUserCreation(){

    }

    private void handleUserUpdate(){

    }
}
