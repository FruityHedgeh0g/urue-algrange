package fr.fruityhedgeh0g.queues.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitConnector {

    Channel openChannel() throws IOException, TimeoutException;

    void openExchange(Channel channel) throws IOException, TimeoutException;

    String initQueue(Channel channel, String queueName) throws IOException, TimeoutException;

    void bindQueue(Channel channel, String queueName, String routingKey) throws IOException, TimeoutException;

}
