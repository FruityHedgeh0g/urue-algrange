package fr.fruityhedgeh0g.exceptions;

public class ForbiddenRoleChangeException extends RuntimeException {
    public ForbiddenRoleChangeException(String message) {
        super(message);
    }
}
