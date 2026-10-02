package fr.fruityhedgeh0g.exceptions;

/** An action the authenticated person is not allowed to take on this resource (checked beyond their Role). */
public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
