package de.butterworks.awscommons.lambdaweb.exceptions;

public class InternalErrorException extends AbstractServerException {
    private static final String PREFIX = "INTERNAL_SERVER_ERROR: ";

    public InternalErrorException(final String s, final Exception e) {
        super(PREFIX + s, e);
    }

    public InternalErrorException(final String s) {
        super(PREFIX + s);
    }
    public InternalErrorException() {
        super(PREFIX + "Internal Server Error");
    }
}
