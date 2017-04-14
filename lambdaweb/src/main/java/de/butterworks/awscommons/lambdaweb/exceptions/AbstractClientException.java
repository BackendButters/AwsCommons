package de.butterworks.awscommons.lambdaweb.exceptions;

public abstract class AbstractClientException extends AbstractWebException {

    public AbstractClientException() {
    }

    public AbstractClientException(final String message) {
        super(message);
    }

    public AbstractClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AbstractClientException(final Throwable cause) {
        super(cause);
    }

    public AbstractClientException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
