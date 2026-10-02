package fr.fruityhedgeh0g.exceptions;

/** Signing up for an Event needs a phone number on the person's profile. */
public class PhoneRequiredException extends RuntimeException {
    public PhoneRequiredException(String message) {
        super(message);
    }
}
