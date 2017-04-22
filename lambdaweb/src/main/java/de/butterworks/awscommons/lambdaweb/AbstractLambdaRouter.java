package de.butterworks.awscommons.lambdaweb;

import com.google.gson.JsonObject;
import de.butterworks.awscommons.lambdaweb.actions.AbstractApiAction;
import de.butterworks.awscommons.lambdaweb.exceptions.ExceptionHandler;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class AbstractLambdaRouter {

    public void doHandle(final InputStream inStream, final OutputStream outStream) {

        try {
            final JsonObject inputJson = SerializationUtil.parseAsJsonElement(IOUtils.toString(inStream, Charset.defaultCharset())).getAsJsonObject();

            final AbstractApiAction apiAction = instantiateAction(inputJson.getAsJsonPrimitive("action").getAsString());
            final AbstractApiResponse responseObject = apiAction.getType() != null ?
                    apiAction.handleGeneric((IntegrationRequestBody)SerializationUtil.fromJson(inputJson.getAsJsonObject("body"), apiAction.getType())) :
                    apiAction.handleGeneric(null);

            if (responseObject != null) {
                IOUtils.write(responseObject.toJson(), outStream, Charset.defaultCharset());
            }
        } catch (final Exception e) {
            ExceptionHandler.processException(e);
        }
    }

    protected abstract AbstractApiAction instantiateAction(final String className);
}
