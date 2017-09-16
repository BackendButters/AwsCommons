package de.butterworks.awscommons.lambdaweb;

import com.google.gson.JsonObject;
import de.butterworks.awscommons.lambdaweb.actions.AbstractApiAction;
import de.butterworks.awscommons.lambdaweb.exceptions.ExceptionHandler;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;
import de.butterworks.awscommons.lambdaweb.integration.UserInfo;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;

public abstract class AbstractLambdaRouter {

    public void doHandle(final InputStream inStream, final OutputStream outStream) throws Exception {

        try {
            final JsonObject inputJson = SerializationUtil.parseAsJsonElement(IOUtils.toString(inStream, Charset.defaultCharset())).getAsJsonObject();

            final AbstractApiAction apiAction = instantiateAction(inputJson.getAsJsonPrimitive("action").getAsString());

            final UserInfo userInfo = new UserInfo(inputJson.getAsJsonPrimitive("uid").getAsString(), inputJson.getAsJsonPrimitive("groups").getAsString());

            final AbstractApiResponse responseObject = apiAction.getType() != null ?
                    apiAction.handleGeneric((IntegrationRequestBody) SerializationUtil.fromJson(inputJson.getAsJsonObject("body"), apiAction.getType()), userInfo) :
                    apiAction.handleGeneric(null, userInfo);

            if (responseObject != null) {
                final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outStream);
                IOUtils.write(responseObject.toJson(), gzipOutputStream, "UTF-8");
                gzipOutputStream.finish();
            }
        } catch (final Exception e) {
            ExceptionHandler.processException(e);
        }
    }

    protected abstract AbstractApiAction instantiateAction(final String className);
}
