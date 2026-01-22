package fr.fruityhedgeh0g.exceptions;

public class MandatoryFieldMissingException extends RuntimeException {
    public MandatoryFieldMissingException(String message) {
        super(message);
    }
}
