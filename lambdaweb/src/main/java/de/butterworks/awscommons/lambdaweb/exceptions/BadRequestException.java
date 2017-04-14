package de.butterworks.awscommons.lambdaweb.exceptions;

public class BadRequestException extends AbstractClientException {
    private static final String PREFIX = "BAD_REQUEST: ";
    public BadRequestException(final String s, final Exception e) {
        super(PREFIX + s, e);
    }

    public BadRequestException(final String s) {
        super(PREFIX + s);
    }
}