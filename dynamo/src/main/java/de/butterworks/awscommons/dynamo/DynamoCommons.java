package de.butterworks.awscommons.dynamo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.amazonaws.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DynamoCommons {

    private static final Logger logger = LoggerFactory.getLogger(DynamoCommons.class);

    private final DynamoDB db;

    private static DynamoCommons instance = null;

    private static final boolean checkTableExistence = System.getenv("checkTableExistence") != null && Boolean.parseBoolean(System.getenv("checkTableExistence"));

    private DynamoCommons() {

        if (StringUtils.isNullOrEmpty(System.getenv("awsAccessKey")) && StringUtils.isNullOrEmpty(System.getProperty("awsAccessKey"))) {
            // local mode
            logger.warn("AWS access key was null or blank. Falling back to localhost:8000");

            db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("obacht", "dummy")))
                    .build());

        } else {
            final String awsAccessKey = System.getenv("awsAccessKey") == null ? System.getProperty("awsAccessKey") : System.getenv("awsAccessKey");
            final String awsSecretKey = System.getenv("awsSecretKey") == null ? System.getProperty("awsSecretKey") : System.getenv("awsSecretKey");
            final Regions region = System.getenv("awsRegion") == null ? Regions.fromName(System.getProperty("awsRegion").toLowerCase()) : Regions.fromName(System.getenv("awsRegion").toLowerCase());

            logger.info("Using Region " + region.name());
            logger.info("Using Access Key " + awsAccessKey);
            logger.info("Checking for table existence? " + checkTableExistence);

            db = new DynamoDB(AmazonDynamoDBClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                    .withRegion(region)
                    .build());
        }
    }

    protected DynamoDB getDb() {
        return db;
    }

    public static synchronized DynamoCommons getInstance() {
        if (instance == null) instance = new DynamoCommons();
        return instance;
    }

    public boolean tableExists(final String tableName) {
        try {
            return TableStatus.ACTIVE.toString().equals(db.getTable(tableName).describe().getTableStatus().toUpperCase());
        } catch (final ResourceNotFoundException rnfe) {
            return false;
        }
    }

    public static boolean getCheckTableExistence() {
        return checkTableExistence;
    }
}
