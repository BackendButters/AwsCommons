package de.butterworks.awscommons.lambdaweb;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;

public final class SerializationUtil {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();

    public static <T> T fromJson(final String input) {
        return gson.fromJson(input, new TypeToken<T>(){}.getType());
    }

    public static <T> T fromJson(final JsonObject input) {
        return gson.fromJson(input, new TypeToken<T>(){}.getType());
    }

    public static <T> String toJson(final T input) {
        return gson.toJson(input);
    }
}
