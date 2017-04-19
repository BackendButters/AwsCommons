package de.butterworks.awscommons.lambdaweb.actions;

import de.butterworks.awscommons.lambdaweb.AbstractApiResponse;
import de.butterworks.awscommons.lambdaweb.exceptions.AbstractWebException;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;

public abstract class AbstractApiAction<T extends IntegrationRequestBody> {

    private final Class<T> type;

    public AbstractApiAction(final Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public abstract AbstractApiResponse handle(final T integrationRequestBody) throws AbstractWebException;
}
