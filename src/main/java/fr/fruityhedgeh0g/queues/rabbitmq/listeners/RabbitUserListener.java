package fr.fruityhedgeh0g.queues.rabbitmq.listeners;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import fr.fruityhedgeh0g.queues.rabbitmq.RabbitConnector;
import fr.fruityhedgeh0g.queues.rabbitmq.consumers.RabbitUserConsumer;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RabbitUserListener {
    @ConfigProperty(name ="rabbitmq.routing.user-deleted") String userDeletionRoute;
    @ConfigProperty(name ="rabbitmq.routing.user-updated") String userUpdateRoute;
    @ConfigProperty(name ="rabbitmq.routing.user-created") String userCreationRoute;

    @ConfigProperty(name ="rabbitmq.queue.user-listener") String queue;
    @Inject RabbitConnector rabbitConnector;
    @Inject InternalUserService internalUserService;

    private RabbitUserConsumer consumer;

    @PostConstruct
    private void init() throws Exception {
        //todo: rendre ce morceau resilient
        String consumerTag = this.getClass().getSimpleName();
        Channel channel = rabbitConnector.openChannel();
        rabbitConnector.openExchange(channel);

        String queueName = rabbitConnector.initQueue(channel,queue);

        rabbitConnector.bindQueue(channel,queueName, userCreationRoute);
        rabbitConnector.bindQueue(channel,queueName, userUpdateRoute);
        rabbitConnector.bindQueue(channel,queueName, userDeletionRoute);

        consumer = new RabbitUserConsumer(
                channel,
                internalUserService,
                userDeletionRoute,
                userUpdateRoute,
                userCreationRoute
        );

        channel.basicConsume(queueName, false,consumerTag,consumer);
    }

}
