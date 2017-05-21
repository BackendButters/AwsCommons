package de.butterworks.awscommons.lambdaweb.integration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class IntegrationRequest {

    private String action;

    private IntegrationRequestBody body;

    private String uid;

    private String groups;

    public IntegrationRequest(final String action, final IntegrationRequestBody body, final String uid, final String groups) {
        this.action = action;
        this.body = body;
        this.uid = uid;
        this.groups = groups;
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

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(final String groups) {
        this.groups = groups;
    }

    public UUID getParsedUid() {
        return UUID.fromString(uid);
    }

    public List<String> getParsedGroups() {
        return  Arrays.stream(groups.split(",")).collect(Collectors.toList());
    }
}
