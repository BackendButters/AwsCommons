package de.butterworks.awscommons.lambdaweb.exceptions;

public class NotFoundException extends AbstractClientException {
    private static final String PREFIX = "NOT_FOUND: ";

    public NotFoundException(final String s, final Exception e) {
        super(PREFIX + s, e);
    }

    public NotFoundException(final String s) {
        super(PREFIX + s);
    }
}