package de.butterworks.awscommons.lambdaweb.integration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UserInfo {

    public UserInfo(final String userId, final String groups) {
        this.userId = userId != null ? UUID.fromString(userId) : null;
        this.groups = groups != null ? Arrays.asList(groups.split(",")) : null;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<String> getGroups() {
        return groups;
    }

    private final UUID userId;

    private final List<String> groups;
}
