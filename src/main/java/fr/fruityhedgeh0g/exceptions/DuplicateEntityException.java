package fr.fruityhedgeh0g.exceptions;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException() { super(); }
}
