package de.butterworks.awscommons.dynamo;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.util.Map;

public interface DynamoConverter<T> {

    Item toDynamo(final T item);

    T fromDynamo(final Map<String, Object> item);
}
