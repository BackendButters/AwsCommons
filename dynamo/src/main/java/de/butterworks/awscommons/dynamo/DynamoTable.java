package de.butterworks.awscommons.dynamo;


import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class DynamoTable<T extends Identifyable> implements CrudInterface<T> {

    private final String tableName;
    private final long readCapacityUnits;
    private final long writeCapacityUnits;
    private final Table table;
    private final DynamoConverter<T> converter;

    private static final Logger logger = LoggerFactory.getLogger(DynamoTable.class);

    public String getTableName() {
        return tableName;
    }

    public long getReadCapacityUnits() {
        return readCapacityUnits;
    }

    public long getWriteCapacityUnits() {
        return writeCapacityUnits;
    }

    public Table getTable() {
        return table;
    }

    public DynamoConverter<T> getConverter() {
        return converter;
    }

    protected DynamoTable(final String tableName,
                          final long readCapacityUnits,
                          final long writeCapacityUnits,
                          final DynamoConverter<T> converter,
                          final boolean checkTableExistence,
                          final List<String> secondaryIndexNames) {

        this.tableName = tableName;
        this.readCapacityUnits = readCapacityUnits;
        this.writeCapacityUnits = writeCapacityUnits;
        this.converter = converter;

        logger.debug(String.format("Initializing table %s with R/W capacity %s / %s", tableName, readCapacityUnits, writeCapacityUnits));

        if(checkTableExistence) {
            if (!DynamoCommons.getInstance().tableExists(tableName)) {
                logger.info("Table " + tableName + " does not exist. Creating...");
                createTable(secondaryIndexNames == null ? new ArrayList<>() : secondaryIndexNames);
            }
        }
        table = DynamoCommons.getInstance().getDb().getTable(tableName);
    }
    protected DynamoTable(final String tableName, final long readCapacityUnits, final long writeCapacityUnits, final DynamoConverter<T> converter) {
        this(tableName, readCapacityUnits, writeCapacityUnits, converter, false, new ArrayList<>());
    }

    public void truncate() {
        for (final T p : getAll()) {
            table.deleteItem(new PrimaryKey("id", p.getId().toString()));
        }
    }

    private void createTable(final List<String> secondaryIndicesNames) {
        try {
            final ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName("id")
                    .withKeyType(KeyType.HASH));

            final ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("id")
                    .withAttributeType("S"));

            final ArrayList<GlobalSecondaryIndex> globalSecondaryIndices = new ArrayList<>();
            for(final String indexName: secondaryIndicesNames) {
                globalSecondaryIndices.add(new GlobalSecondaryIndex()
                        .withIndexName(indexName + "-index")
                        .withKeySchema(new KeySchemaElement().withAttributeName(indexName).withKeyType(KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(new ProvisionedThroughput()
                                .withReadCapacityUnits(readCapacityUnits)
                                .withWriteCapacityUnits(writeCapacityUnits)));
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withGlobalSecondaryIndexes(globalSecondaryIndices)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(readCapacityUnits)
                            .withWriteCapacityUnits(writeCapacityUnits));

            request.setAttributeDefinitions(attributeDefinitions);

            logger.debug("Issuing CreateTable request for " + tableName);
            final Table table = DynamoCommons.getInstance().getDb().createTable(request);
            logger.debug("Waiting for table to be created...");
            table.waitForActive();

        } catch (final Exception e) {
            logger.error("CreateTable request failed: " + e.getMessage());
        }
    }

    public Optional<T> get(final UUID id) {
        final Item dynamoResult = table.getItem("id", id.toString());

        if (dynamoResult == null) {
            return Optional.empty();
        } else {

            final Map<String, Object> result = dynamoResult.asMap();
            return Optional.of(converter.fromDynamo(result));
        }
    }

    public List<T> getAll() {

        final ItemCollection<ScanOutcome> items = table.scan(new ScanSpec());
        final List<T> rslt = new ArrayList<>();
        final Iterator<Item> iter = items.iterator();

        while (iter.hasNext()) {
            final Map<String, Object> result = iter.next().asMap();

            rslt.add(converter.fromDynamo(result));
        }
        return rslt;
    }

    public void add(final T item) {
        table.putItem(converter.toDynamo(item));
    }

    public void update(final T item) {
        table.putItem(converter.toDynamo(item));
    }
}
