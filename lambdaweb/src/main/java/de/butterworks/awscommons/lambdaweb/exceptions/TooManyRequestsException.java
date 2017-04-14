package de.butterworks.awscommons.lambdaweb.exceptions;

public class TooManyRequestsException extends AbstractClientException {
    private static final String PREFIX = "TOO_MANY_REQUESTS: ";
    public TooManyRequestsException(final String s, final Exception e) {
        super(PREFIX + s, e);
    }

    public TooManyRequestsException(final String s) {
        super(PREFIX + s);
    }
}