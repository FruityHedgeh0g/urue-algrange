package fr.fruityhedgeh0g.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Decorator
@Priority(100)
public class RabbitConnectorLogProxy implements RabbitConnector{

    @Inject
    @Delegate
    RabbitConnector connector;

    @Override
    public Channel openChannel() throws IOException, TimeoutException {
        return Try.of(connector::openChannel)
                .onFailure(t -> {
                    switch (t){
                        case IOException e -> Log.errorf("Error opening channel: %s", e.getMessage());
                        case TimeoutException e -> Log.errorf("Queue connection timed out: %s", e.getMessage());
                        default -> Log.errorf("Queue unexpected error: %s", t.getMessage());
                    }
                }).get();
    }
}
