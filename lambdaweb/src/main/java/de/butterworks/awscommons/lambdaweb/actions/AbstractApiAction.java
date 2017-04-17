package de.butterworks.awscommons.lambdaweb.actions;

import de.butterworks.awscommons.lambdaweb.AbstractApiResponse;
import de.butterworks.awscommons.lambdaweb.exceptions.AbstractWebException;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;

public abstract class AbstractApiAction<T extends IntegrationRequestBody> {

    public abstract AbstractApiResponse handle(final T integrationRequestBody) throws AbstractWebException;
}
