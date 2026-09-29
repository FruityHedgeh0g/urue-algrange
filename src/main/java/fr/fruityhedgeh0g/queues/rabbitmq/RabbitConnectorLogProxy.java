package fr.fruityhedgeh0g.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Decorator
@Priority(100)
public class RabbitConnectorLogProxy implements RabbitConnector{

    @Inject
    @Delegate
    RabbitConnector connector;

    @ConfigProperty(name = "rabbitmq.exchange")
    String exchange;

    @Override
    public Channel openChannel() throws IOException, TimeoutException {
        Log.info("Opening new inter-service communication channel");
        return Try.of(connector::openChannel)
                .onSuccess(v -> Log.info("Channel opened successfully"))
                .onFailure(t -> {
                    switch (t){
                        case IOException e -> Log.errorf("Error opening channel: %s", e.getMessage());
                        case TimeoutException e -> Log.errorf("Connection timed out: %s", e.getMessage());
                        default -> Log.errorf("Unexpected error: %s", t.getMessage());
                    }
                }).get();
    }

    @Override
    public void openExchange(Channel channel) throws IOException, TimeoutException {
        Log.infof("Opening %s exchange",exchange);
        Try.run(() -> connector.openExchange(channel))
                .onSuccess(v -> Log.infof("Exchange %s opened successfully",exchange))
                .onFailure(t -> {
                    switch (t){
                        case IOException e -> Log.errorf("Error opening exchange: %s", e.getMessage());
                        case TimeoutException e -> Log.errorf("Connection timed out: %s", e.getMessage());
                        default -> Log.errorf("Unexpected error: %s", t.getMessage());
                    }
                }).get();
    }

    @Override
    public String initQueue(Channel channel, String queueName) throws IOException, TimeoutException {
        Log.infof("Initializing queue %s", queueName);
        return Try.of(() -> connector.initQueue(channel, queueName))
                .onSuccess(queue -> Log.infof("Queue %s initialized",queue))
                .onFailure(t -> {
                    switch (t){
                        case IOException e -> Log.errorf("Error initializing queue: %s", e.getMessage());
                        case TimeoutException e -> Log.errorf("Connection timed out: %s", e.getMessage());
                        default -> Log.errorf("Unexpected error: %s", t.getMessage());
                    }
                }).get();
    }

    @Override
    public void bindQueue(Channel channel, String queueName, String routingKey) throws IOException, TimeoutException {
        Log.infof("Binding exchange %s to queue %s with routing key %s.",exchange,queueName,routingKey);
        Try.run(() -> connector.bindQueue(channel,queueName,routingKey))
                .onSuccess(v -> Log.infof("Exchange %s bound to queue %s with routing key %s",exchange,queueName,routingKey))
                .onFailure(t -> {
                    switch (t){
                        case IOException e -> Log.errorf("Error binding exchange to queue: %s", e.getMessage());
                        case TimeoutException e -> Log.errorf("Connection timed out: %s", e.getMessage());
                        default -> Log.errorf("Unexpected error: %s", t.getMessage());
                    }
                }).get();
    }
}
