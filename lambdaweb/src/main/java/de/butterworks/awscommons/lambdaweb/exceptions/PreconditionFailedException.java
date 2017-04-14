package de.butterworks.awscommons.lambdaweb.exceptions;

public class PreconditionFailedException extends AbstractClientException {
    private static final String PREFIX = "PREQ: ";
    public PreconditionFailedException(final String s, final Exception e) {
        super(PREFIX + s, e);
    }

    public PreconditionFailedException(final String s) {
        super(PREFIX + s);
    }
}