package de.butterworks.awscommons.lambdaweb;

public abstract class AbstractApiResponse {

    public String toJson() {
        return SerializationUtil.toJson(this);
    }
}
