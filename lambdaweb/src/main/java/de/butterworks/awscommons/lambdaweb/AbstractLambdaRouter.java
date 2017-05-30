package de.butterworks.awscommons.lambdaweb;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.google.gson.JsonObject;
import de.butterworks.awscommons.lambdaweb.actions.AbstractApiAction;
import de.butterworks.awscommons.lambdaweb.exceptions.ExceptionHandler;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;
import de.butterworks.awscommons.lambdaweb.integration.UserInfo;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class AbstractLambdaRouter {

    public void doHandle(final InputStream inStream, final OutputStream outStream) {

        final Subsegment handlingSegment = AWSXRay.beginSubsegment("Handling");
        try {

            final JsonObject inputJson = SerializationUtil.parseAsJsonElement(IOUtils.toString(inStream, Charset.defaultCharset())).getAsJsonObject();

            final AbstractApiAction apiAction = instantiateAction(inputJson.getAsJsonPrimitive("action").getAsString());

            final UserInfo userInfo = new UserInfo(inputJson.getAsJsonPrimitive("uid").getAsString(), inputJson.getAsJsonPrimitive("groups").getAsString());

            final AbstractApiResponse responseObject = apiAction.getType() != null ?
                    apiAction.handleGeneric((IntegrationRequestBody)SerializationUtil.fromJson(inputJson.getAsJsonObject("body"), apiAction.getType()), userInfo) :
                    apiAction.handleGeneric(null, userInfo);

            if (responseObject != null) {
                IOUtils.write(responseObject.toJson(), outStream, Charset.defaultCharset());
            }
        } catch (final Exception e) {
            handlingSegment.addException(e);
            ExceptionHandler.processException(e);
        } finally {
            AWSXRay.endSubsegment();
        }
    }

    protected abstract AbstractApiAction instantiateAction(final String className);
}
