package de.butterworks.awscommons.lambdaweb;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.DateFormat;

public final class SerializationUtil {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();

    private static final JsonParser jsonParser = new JsonParser();

    public static <T> T fromJson(final String input) throws IOException {
        return gson.fromJson(input, new TypeToken<T>(){}.getType());
    }

    public static <T> T fromJson(final JsonObject input) {
        return gson.fromJson(input, new TypeToken<T>(){}.getType());
    }

    public static <T> T fromJson(final String input, final Class<T> clazz) {
        return gson.fromJson(input, clazz);
    }

    public static <T> T fromJson(final JsonObject input, final Class<T> clazz) {
        return gson.fromJson(input, clazz);
    }

    public static <T> T fromJson(final JsonElement input) {
        return gson.fromJson(input, new TypeToken<T>(){}.getType());
    }

    public static JsonElement parseAsJsonElement(final String input) {
        return jsonParser.parse(input);
    }

    public static <T> String toJson(final T input) {
        return gson.toJson(input);
    }

    public static <T> JsonElement toJsonElement(final T input) {
        return gson.toJsonTree(input);
    }
}
