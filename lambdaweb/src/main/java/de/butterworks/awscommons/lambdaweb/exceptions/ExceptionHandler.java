package de.butterworks.awscommons.lambdaweb.exceptions;

import com.getsentry.raven.Raven;
import com.getsentry.raven.RavenFactory;
import com.getsentry.raven.dsn.Dsn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {

private static final Raven raven = System.getenv("ravenDsn") == null ? null : RavenFactory.ravenInstance(new Dsn(System.getenv("ravenDsn")));
private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private static void logException(final Throwable ex) {
        if(raven != null ) {
            logger.debug("Reporting exception to Sentry");
            raven.sendException(ex);
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