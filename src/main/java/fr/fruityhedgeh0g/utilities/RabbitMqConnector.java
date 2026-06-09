package fr.fruityhedgeh0g.utilities;

import com.rabbitmq.client.ConnectionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RabbitMqConnector {

    @Getter
    private final ConnectionFactory connectionFactory;

    RabbitMqConnector(
            @ConfigProperty(name = "rabbitmq.username") String username,
            @ConfigProperty(name = "rabbitmq.password") String password,
            @ConfigProperty(name = "rabbitmq.virtual.host") String virtualHost,
            @ConfigProperty(name = "rabbitmq.host") String host,
            @ConfigProperty(name = "rabbitmq.port") int port
    ) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setUsername(username);
        this.connectionFactory.setPassword(password);
        this.connectionFactory.setVirtualHost(virtualHost);
        this.connectionFactory.setHost(host);
        this.connectionFactory.setPort(port);


    }
}
