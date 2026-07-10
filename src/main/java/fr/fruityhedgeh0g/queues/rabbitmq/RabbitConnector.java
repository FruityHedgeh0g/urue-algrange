package fr.fruityhedgeh0g.queues.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitConnector {

    Channel openChannel() throws IOException, TimeoutException;

}
