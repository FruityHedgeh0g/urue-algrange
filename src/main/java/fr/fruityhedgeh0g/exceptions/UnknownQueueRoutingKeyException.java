package fr.fruityhedgeh0g.exceptions;

public class UnknownQueueRoutingKeyException extends RuntimeException {
    public UnknownQueueRoutingKeyException(String message) {
        super(message);
    }
}
