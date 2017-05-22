package de.butterworks.awscommons.lambdaweb.actions;

import de.butterworks.awscommons.lambdaweb.AbstractApiResponse;
import de.butterworks.awscommons.lambdaweb.exceptions.AbstractWebException;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;
import de.butterworks.awscommons.lambdaweb.integration.UserInfo;

public abstract class AbstractApiAction<T extends IntegrationRequestBody> {

    private final Class<T> type;

    public AbstractApiAction(final Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public AbstractApiResponse handleGeneric(final IntegrationRequestBody integrationRequestBody, final UserInfo userInfo) throws AbstractWebException {
        return handle((T)integrationRequestBody, userInfo);
    }

    public abstract AbstractApiResponse handle(final T integrationRequestBody, final UserInfo userInfo) throws AbstractWebException;
}
