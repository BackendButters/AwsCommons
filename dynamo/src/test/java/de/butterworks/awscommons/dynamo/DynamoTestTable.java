package de.butterworks.awscommons.dynamo;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.common.collect.Lists;

import java.util.*;

class DynamoTestTable extends AbstractDynamoTable<TestEntity> {

    protected static final String tableName = "testtable";

    protected DynamoTestTable() {

        super(tableName,
                1,
                1,
                new TestEntityConverter(),
                true,
                Lists.newArrayList("dummySecondaryIndex"));
    }
}
class TestEntity implements Identifyable {

    private final UUID id;
    private final String dummyField;
    private final String dummySecondaryIndex;

    public TestEntity(UUID id, String dummyField, String dummySecondaryIndex) {
        this.id = id;
        this.dummyField = dummyField;
        this.dummySecondaryIndex = dummySecondaryIndex;
    }

    public String getDummySecondaryIndex() {
        return dummySecondaryIndex;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getDummyField() {
        return dummyField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestEntity that = (TestEntity) o;

        if (!id.equals(that.id)) return false;
        if (dummyField != null ? !dummyField.equals(that.dummyField) : that.dummyField != null) return false;
        return dummySecondaryIndex.equals(that.dummySecondaryIndex);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (dummyField != null ? dummyField.hashCode() : 0);
        result = 31 * result + dummySecondaryIndex.hashCode();
        return result;
    }
}
class TestEntityConverter implements DynamoConverter<TestEntity> {

    @Override
    public Item toDynamo(TestEntity item) {
        return new Item().withPrimaryKey("id", item.getId().toString())
                .withString("dummyField", item.getDummyField())
                .withString("dummySecondaryIndex", item.getDummySecondaryIndex());
    }

    @Override
    public TestEntity fromDynamo(Map<String, Object> item) {
        return new TestEntity((UUID.fromString((String) item.get("id"))),
                (String) item.get("dummyField"),
                (String) item.get("dummySecondaryIndex"));
    }
}
