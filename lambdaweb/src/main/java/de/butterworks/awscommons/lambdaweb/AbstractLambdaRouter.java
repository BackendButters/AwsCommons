package de.butterworks.awscommons.lambdaweb;

import de.butterworks.awscommons.lambdaweb.actions.AbstractApiAction;
import de.butterworks.awscommons.lambdaweb.exceptions.ExceptionHandler;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequest;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class AbstractLambdaRouter {

    public void doHandle(final InputStream inStream, final OutputStream outStream) {

        try {
            final IntegrationRequest integrationRequest = SerializationUtil.fromJson(IOUtils.toString(inStream, Charset.defaultCharset()));

            final AbstractApiResponse responseObject = instantiateAction(integrationRequest.getAction()).handle(integrationRequest.getBody());

            if (responseObject != null) {
                IOUtils.write(responseObject.toJson(), outStream, Charset.defaultCharset());
            }
        } catch (final Exception e) {
            ExceptionHandler.processException(e);
        }
    }

    protected abstract AbstractApiAction instantiateAction(final String className);
}
