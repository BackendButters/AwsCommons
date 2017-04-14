package de.butterworks.awscommons.lambdaweb.exceptions;

public abstract class AbstractServerException extends AbstractWebException {
    public AbstractServerException() {
    }

    public AbstractServerException(final String message) {
        super(message);
    }

    public AbstractServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AbstractServerException(final Throwable cause) {
        super(cause);
    }

    public AbstractServerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
