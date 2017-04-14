package de.butterworks.awscommons.lambdaweb.exceptions;

public abstract class AbstractWebException extends RuntimeException {

    public AbstractWebException() {
    }

    public AbstractWebException(final String message) {
        super(message);
    }

    public AbstractWebException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AbstractWebException(final Throwable cause) {
        super(cause);
    }

    public AbstractWebException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
