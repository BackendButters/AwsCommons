package de.butterworks.awscommons.dynamo;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.butterworks.awscommons.dynamo.DynamoTestTable.tableName;
import static org.assertj.core.api.Assertions.assertThat;

public class DynamoTableTest {

    private DynamoTestTable dynamoTestTable = new DynamoTestTable();

    private static final Logger logger = LoggerFactory.getLogger(DynamoTableTest.class);

    private static void deleteTableIfPresent() {
        try {
            logger.info("Deleting table if present...");
            DynamoCommons.getInstance().getDb().getTable(DynamoTestTable.tableName).delete();
            DynamoCommons.getInstance().getDb().getTable(DynamoTestTable.tableName).waitForDelete();
        } catch(final ResourceNotFoundException | InterruptedException ex) {

        }
    }

    @BeforeClass
    public static void beforeClass() {
        deleteTableIfPresent();
    }

    @AfterClass
    public static void afterClass() {
        deleteTableIfPresent();
    }

    @Before
    public void before() {
        dynamoTestTable.truncate();
    }

    @Test
    public void testAdd() {

        dynamoTestTable.add(new TestEntity(UUID.randomUUID(), "obacht", "huch"));
    }

    @Test
    public void testGet() {
        assertThat(dynamoTestTable.get(UUID.randomUUID()).isPresent()).isFalse();

        final TestEntity t = new TestEntity(UUID.randomUUID(), "obacht", "huch");
        dynamoTestTable.add(t);

        final Optional<TestEntity> retrieved = dynamoTestTable.get(t.getId());
        assertThat(retrieved.isPresent()).isTrue();
        assertThat(t).isEqualTo(retrieved.get());
    }

    @Test
    public void testGetAll() {
        assertThat(dynamoTestTable.getAll()).isEmpty();

        final TestEntity t1 = new TestEntity(UUID.randomUUID(), "obacht", "huch");
        final TestEntity t2 = new TestEntity(UUID.randomUUID(), "obacht", "huch");
        dynamoTestTable.add(t1);
        dynamoTestTable.add(t2);

        final List<TestEntity> retrieved = dynamoTestTable.getAll();
        assertThat(retrieved).hasSize(2);
        assertThat(retrieved).contains(t1, t2);
    }

    @Test
    public void testUpdate() {

        final TestEntity t = new TestEntity(UUID.randomUUID(), "obacht", "huch");
        dynamoTestTable.add(t);

        final Optional<TestEntity> retrieved = dynamoTestTable.get(t.getId());
        assertThat(retrieved.isPresent()).isTrue();
        assertThat(t).isEqualTo(retrieved.get());

        final TestEntity updatedT = new TestEntity(t.getId(), "HANA", "huch");
        dynamoTestTable.update(updatedT);
        final Optional<TestEntity> retrievedUpdated = dynamoTestTable.get(t.getId());
        assertThat(retrievedUpdated.isPresent()).isTrue();
        assertThat(updatedT).isEqualTo(retrievedUpdated.get());
    }

    @Test
    public void testGetBySecondaryIndex() {

        final TestEntity t1 = new TestEntity(UUID.randomUUID(), "obacht", "huch");
        final TestEntity t2 = new TestEntity(UUID.randomUUID(), "obacht", "huch");
        final TestEntity t3 = new TestEntity(UUID.randomUUID(), "obacht", "HANA");
        dynamoTestTable.add(t1);
        dynamoTestTable.add(t2);
        dynamoTestTable.add(t3);

        final List<TestEntity> retrieved = dynamoTestTable.getBySecondaryIndex("huch", dynamoTestTable.getSecondaryIndexNames().get(0));
        assertThat(retrieved).containsExactlyInAnyOrder(t1, t2);
    }
}
