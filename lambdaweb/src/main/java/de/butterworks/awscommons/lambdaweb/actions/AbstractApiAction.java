package de.butterworks.awscommons.lambdaweb.actions;

import com.google.gson.JsonObject;
import de.butterworks.awscommons.lambdaweb.SerializationUtil;
import de.butterworks.awscommons.lambdaweb.exceptions.AbstractWebException;

public abstract class AbstractApiAction<T> {

    protected T getRequestEntity() {
        return requestEntity;
    }

    private final T requestEntity;

    public AbstractApiAction(final JsonObject requestJson) {
        requestEntity = SerializationUtil.fromJson(requestJson);
    }

    public AbstractApiAction() {
        requestEntity = null;
    }

    /**
     * Handle the request based on the request
     * @return JSON serialized result
     * @throws AbstractWebException
     */
    public abstract String handle() throws AbstractWebException;
}
