package de.butterworks.awscommons.dynamo;


import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractDynamoTable<T extends Identifyable> implements CrudInterface<T> {

    private final String tableName;
    private final long readCapacityUnits;
    private final long writeCapacityUnits;
    private final Table table;
    private final DynamoConverter<T> converter;
    private final List<String> secondaryIndexNames;

    private static final Logger logger = LoggerFactory.getLogger(AbstractDynamoTable.class);

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

    public List<String> getSecondaryIndexNames() {
        return secondaryIndexNames;
    }

    public DynamoConverter<T> getConverter() {
        return converter;
    }

    protected AbstractDynamoTable(final String tableName,
                                  final long readCapacityUnits,
                                  final long writeCapacityUnits,
                                  final DynamoConverter<T> converter,
                                  final boolean checkTableExistence,
                                  final List<String> secondaryIndexNames) {

        this.tableName = tableName;
        this.readCapacityUnits = readCapacityUnits;
        this.writeCapacityUnits = writeCapacityUnits;
        this.converter = converter;
        this.secondaryIndexNames = secondaryIndexNames;

        logger.debug(String.format("Initializing table %s with R/W capacity %s / %s", tableName, readCapacityUnits, writeCapacityUnits));

        if (checkTableExistence) {
            if (!DynamoCommons.getInstance().tableExists(tableName)) {
                logger.info("Table " + tableName + " does not exist. Creating...");
                createTable();
            }
        }
        table = DynamoCommons.getInstance().getDb().getTable(tableName);
    }

    protected AbstractDynamoTable(final String tableName, final long readCapacityUnits, final long writeCapacityUnits, final DynamoConverter<T> converter) {
        this(tableName, readCapacityUnits, writeCapacityUnits, converter, false, new ArrayList<>());
    }

    private void createTable() {
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
            for (final String indexName : secondaryIndexNames) {
                attributeDefinitions.add(new AttributeDefinition()
                        .withAttributeName(indexName)
                        .withAttributeType("S"));

                globalSecondaryIndices.add(new GlobalSecondaryIndex()
                        .withIndexName(indexName + "-index")
                        .withKeySchema(new KeySchemaElement().withAttributeName(indexName).withKeyType(KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(new ProvisionedThroughput()
                                .withReadCapacityUnits(readCapacityUnits)
                                .withWriteCapacityUnits(writeCapacityUnits)));
            }

            final CreateTableRequest request = new CreateTableRequest()
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
            return Optional.of(converter.fromDynamo(dynamoResult.asMap()));
        }
    }

    public List<T> getAll() {
        //ToDo: implement batching to counter large tables and Dynamo request limits
        return StreamSupport.stream(table.scan(new ScanSpec()).spliterator(), true)
                .map(i -> converter.fromDynamo(i.asMap()))
                .collect(Collectors.toList());
    }

    public void add(final T item) {
        table.putItem(converter.toDynamo(item));
    }

    public void update(final T item) {
        table.putItem(converter.toDynamo(item));
    }

    public void truncate() {
        getAll()
                .parallelStream()
                .forEach(p -> table.deleteItem(new PrimaryKey("id", p.getId().toString())));
    }

    public List<T> getBySecondaryIndex(final String queryParameter, final String secondaryIndexName) {
        return StreamSupport.stream(table.getIndex(secondaryIndexName + "-index").query(secondaryIndexName, queryParameter).spliterator(), false)
                .map(i -> converter.fromDynamo(i.asMap()))
                .collect(Collectors.toList());
    }
}
