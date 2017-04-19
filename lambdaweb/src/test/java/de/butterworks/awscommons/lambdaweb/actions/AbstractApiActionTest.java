package de.butterworks.awscommons.lambdaweb.actions;

import com.google.gson.JsonObject;
import de.butterworks.awscommons.lambdaweb.SerializationUtil;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequest;
import de.butterworks.awscommons.lambdaweb.integration.IntegrationRequestBody;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractApiActionTest {

    @Test
    public void testAbstractApiActionSerialization() throws IOException {

        final IntegrationRequest originalIntegrationRequest = new IntegrationRequest("TestAction", new TestIntegrationRequestBody("MyFieldValue"));
        final String serializedIntegrationRequest = SerializationUtil.toJson(originalIntegrationRequest);

        final JsonObject parsedJson = SerializationUtil.parseAsJsonElement(serializedIntegrationRequest).getAsJsonObject();

        final String action = parsedJson.getAsJsonPrimitive("action").getAsString();
        final TestIntegrationRequestBody body = SerializationUtil.fromJson(parsedJson.getAsJsonObject("body"), TestIntegrationRequestBody.class);

        assertThat(originalIntegrationRequest).isEqualToComparingFieldByField(new IntegrationRequest(action, body));
    }

    private class TestIntegrationRequestBody implements IntegrationRequestBody {

        String testField;

        public TestIntegrationRequestBody(String testField) {
            this.testField = testField;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestIntegrationRequestBody that = (TestIntegrationRequestBody) o;

            return testField != null ? testField.equals(that.testField) : that.testField == null;

        }

        @Override
        public int hashCode() {
            return testField != null ? testField.hashCode() : 0;
        }
    }
}
