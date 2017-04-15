package de.butterworks.awscommons.dynamo;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DynamoCommons {

    private static final Logger logger = LoggerFactory.getLogger(DynamoCommons.class);

    private final AmazonDynamoDBClient client;

    private static DynamoCommons instance = null;

    private final DynamoDB db;

    private final static String awsAccessKey = System.getenv("accessKey");
    private final static String awsSecretKey = System.getenv("secretKey");
    private final static Regions region = Regions.fromName(System.getenv("region").toUpperCase());

    private DynamoCommons() {
        logger.info("Using Region " + region.name());
        logger.info("Using Access Key " + awsAccessKey);

        client = new AmazonDynamoDBClient(new BasicAWSCredentials(awsAccessKey, awsSecretKey))
                .withRegion(region);

        db = new DynamoDB(new AmazonDynamoDBClient(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey))
                .withRegion(region));
    }

    DynamoDB getDb() {
        return db;
    }

    static synchronized DynamoCommons getInstance() {
        if (instance == null) instance = new DynamoCommons();
        return instance;
    }

    boolean tableExists(final String tableName) {
        try {
            final TableDescription table = client.describeTable(new DescribeTableRequest(tableName))
                    .getTable();
            return TableStatus.ACTIVE.toString().equals(table.getTableStatus().toUpperCase());
        } catch (final ResourceNotFoundException rnfe) {
            return false;
        }
    }
}
