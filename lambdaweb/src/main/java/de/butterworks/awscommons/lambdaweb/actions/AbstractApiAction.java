package de.butterworks.awscommons.lambdaweb.actions;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.butterworks.awscommons.lambdaweb.exceptions.AbstractWebException;

import java.text.DateFormat;

public abstract class AbstractApiAction<T> {

    private static final Gson gson = new GsonBuilder()
        .setDateFormat(DateFormat.LONG)
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create();

    protected Gson getGson() {
        return gson;
    }

    protected T getRequestEntity() {
        return requestEntity;
    }

    private final T requestEntity;

    public AbstractApiAction(final JsonObject requestJson) {
        requestEntity = getGson().fromJson(requestJson, new TypeToken<T>(){}.getType());
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
