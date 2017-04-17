package de.butterworks.awscommons.lambdaweb;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SerializationUtilTest {

    @Test
    public void testSerialization() throws Exception {
        assertThat(SerializationUtil.toJson(new SerializationTestClass("test")))
                .isEqualTo(SerializationUtil.toJson(SerializationUtil.fromJson("{\"name\":\"test\"}")));
    }

    private class SerializationTestClass {
        private String name;

        SerializationTestClass(String name) {
            this.name = name;
        }
    }
}