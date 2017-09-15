package de.butterworks.awscommons.lambdaweb.exceptions;

import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {

private static final SentryClient sentry = System.getenv("sentryDsn") == null ? null : SentryClientFactory.sentryClient(System.getenv("sentryDsn"));
private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private static void logException(final Throwable ex) {
        if(sentry != null ) {
            logger.debug("Reporting exception to Sentry");
            sentry.sendException(ex);
        }
    }

    public static void processException(final Exception e) throws Exception {

        if(!(e instanceof AbstractClientException)) {
            logger.error("Error encountered: " + e.getMessage(), e);
            logException(e);
        } else {
            logger.debug("Client error encountered: " + e.getMessage(), e);
        }
        throw e;
    }
}