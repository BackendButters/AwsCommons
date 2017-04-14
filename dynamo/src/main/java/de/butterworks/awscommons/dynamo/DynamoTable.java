package de.butterworks.awscommons.dynamo;


import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

abstract class DynamoTable<T extends Identifyable> implements CrudInterface<T> {

    protected final String tableName;
    protected final long readCapacityUnits;
    protected final long writeCapacityUnits;
    protected final Table table;
    protected final DynamoConverter<T> converter;
    protected final DynamoDB db;

    private static final Logger logger = LoggerFactory.getLogger(DynamoTable.class);

    protected DynamoTable(final String tableName, final long readCapacityUnits, final long writeCapacityUnits, final DynamoConverter<T> converter, final boolean checkTableExistence) {
        this.tableName = tableName;
        this.readCapacityUnits = readCapacityUnits;
        this.writeCapacityUnits = writeCapacityUnits;
        this.converter = converter;

        logger.debug(String.format("Initializing table %s with R/W capacity %s / %s", tableName, readCapacityUnits, writeCapacityUnits));

        if(checkTableExistence) {
            if (!Commons.getInstance().tableExists(tableName)) {
                logger.info("Table " + tableName + " does not exist. Creating...");
                createTable();
            }
        }
        table = Commons.getInstance().getDb().getTable(tableName);
        db = Commons.getInstance().getDb();
    }
    protected DynamoTable(final String tableName, final long readCapacityUnits, final long writeCapacityUnits, final DynamoConverter<T> converter) {
        this(tableName, readCapacityUnits, writeCapacityUnits, converter, false);
    }

    public void truncate() {
        for (final T p : getAll()) {
            table.deleteItem(new PrimaryKey("id", p.getId().toString()));
        }
    }

    public void createTable() {
        try {
            final ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName("id")
                    .withKeyType(KeyType.HASH));

            final ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("id")
                    .withAttributeType("S"));

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(readCapacityUnits)
                            .withWriteCapacityUnits(writeCapacityUnits));

            request.setAttributeDefinitions(attributeDefinitions);

            logger.debug("Issuing CreateTable request for " + tableName);
            final Table table = Commons.getInstance().getDb().createTable(request);
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
