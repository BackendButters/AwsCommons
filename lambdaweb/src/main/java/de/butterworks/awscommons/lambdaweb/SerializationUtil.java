package de.butterworks.awscommons.lambdaweb;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
