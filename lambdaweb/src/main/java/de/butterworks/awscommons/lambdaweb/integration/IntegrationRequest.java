package de.butterworks.awscommons.lambdaweb.integration;

public class IntegrationRequest {

    private String action;

    private IntegrationRequestBody body;

    public IntegrationRequest(final String action, final IntegrationRequestBody body) {
        this.action = action;
        this.body = body;
    }

    public IntegrationRequest() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public IntegrationRequestBody getBody() {
        return body;
    }

    public void setBody(IntegrationRequestBody body) {
        this.body = body;
    }
}
