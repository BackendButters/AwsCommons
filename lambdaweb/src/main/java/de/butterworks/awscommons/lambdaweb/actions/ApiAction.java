package de.butterworks.awscommons.lambdaweb.actions;

import com.google.gson.JsonObject;
import de.butterworks.awscommons.lambdaweb.exceptions.AbstractWebException;

public interface ApiAction {

    String handle(final JsonObject request) throws AbstractWebException;
}
