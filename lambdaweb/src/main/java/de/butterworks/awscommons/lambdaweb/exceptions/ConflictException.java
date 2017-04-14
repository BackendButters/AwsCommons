package de.butterworks.awscommons.lambdaweb.exceptions;

public class ConflictException extends AbstractClientException {
    private static final String PREFIX = "CONFLICT: ";
    public ConflictException(final String s, final Exception e) {
        super(PREFIX + s, e);
    }

    public ConflictException(final String s) {
        super(PREFIX + s);
    }
}