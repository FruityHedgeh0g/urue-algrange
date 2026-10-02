package fr.fruityhedgeh0g.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import fr.fruityhedgeh0g.exceptions.QueueConnectionException;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class RabbitConnectorImpl implements RabbitConnector {

    private final ConnectionFactory connectionFactory;
    private Connection connection;

    private final String exchange;

    RabbitConnectorImpl(
            @ConfigProperty(name = "rabbitmq.username") String username,
            @ConfigProperty(name = "rabbitmq.password") String password,
            @ConfigProperty(name = "rabbitmq.virtual.host") String virtualHost,
            @ConfigProperty(name = "rabbitmq.host") String host,
            @ConfigProperty(name = "rabbitmq.port") int port,
            @ConfigProperty(name = "rabbitmq.exchange") String exchange
    ) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setUsername(username);
        this.connectionFactory.setPassword(password);
        this.connectionFactory.setVirtualHost(virtualHost);
        this.connectionFactory.setHost(host);
        this.connectionFactory.setPort(port);
        this.connectionFactory.setAutomaticRecoveryEnabled(true);
        this.exchange = exchange;

    }

    private synchronized Connection openConnection() throws IOException, TimeoutException {
        if (connection == null || !connection.isOpen()) connection =  this.connectionFactory.newConnection();
        return this.connection;
    }

    public Channel openChannel() throws IOException, TimeoutException {
        Connection myConnection = openConnection();
        return myConnection.createChannel();

    }

    public void openExchange(Channel channel) throws IOException, TimeoutException {
        channel.exchangeDeclare(exchange, "direct", true);
    }

    public String initQueue(Channel channel,String queue) throws IOException, TimeoutException {
        return channel.queueDeclare(queue,true,false,false,null).getQueue();
    }

    public void bindQueue(Channel channel, String queueName, String routingKey) throws IOException, TimeoutException {
        channel.queueBind(queueName, exchange, routingKey);
    }
}
