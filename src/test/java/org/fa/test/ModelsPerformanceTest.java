package org.fa.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

public class ModelsPerformanceTest {
    private static final int testAttempts = 10_000_000;
    private final String inputJson;

    public ModelsPerformanceTest() throws IOException {
        inputJson = IOUtils.toString(
                ModelsPerformanceTest.class.getResourceAsStream("input.json"), "UTF-8");
    }

    @Test
    public void testParse() throws IOException {
        long jacksonDeserializationNanos = testAverageJacksonDeserializationTime();
        System.out.println("Jackson Deserialization\t= " + jacksonDeserializationNanos + " ns");
        long jsonObjectDeserializationNanos = testAverageJsonObjectDeserializationTime();
        System.out.println("JSON Object Deserialization\t= " + jsonObjectDeserializationNanos + " ns");
    }

    @Test
    public void testReadAccess() throws IOException {
        long javaObjectReadAccessNanos = testAverageJavaObjectReadAccessTime();
        System.out.println("Java Object Read Access\t= " + javaObjectReadAccessNanos + " ns");
        long jsonObjectReadAccessNanos = testAverageJsonObjectReadAccessTime();
        System.out.println("JSON Object Read Access\t= " + jsonObjectReadAccessNanos + " ns");
    }

    @Test
    public void testWriteAccess() {
        long javaObjectReadAccessNanos = testAverageJavaObjectWriteAccessTime();
        System.out.println("Java Object Write Access\t= " + javaObjectReadAccessNanos + " ns");
        long jsonObjectReadAccessNanos = testAverageJsonObjectWriteAccessTime();
        System.out.println("JSON Object Write Access\t= " + jsonObjectReadAccessNanos + " ns");
    }

    private long testAverageJavaObjectWriteAccessTime() {
        SimpleModel.SimpleModelBuilder builder = SimpleModel.builder();
        String value = "some string";
        final long start = System.nanoTime();
        for (int i = 0; i < testAttempts; i++) {
            builder.field6(value);
        }
        final long end = System.nanoTime();
        return (end - start)/testAttempts;
    }

    private long testAverageJsonObjectWriteAccessTime() {
        JSONObject jsonObject = new JSONObject();
        String value = "some string";
        final long start = System.nanoTime();
        for (int i = 0; i < testAttempts; i++) {
            jsonObject.put("field6", value);
        }
        final long end = System.nanoTime();
        return (end - start)/testAttempts;
    }

    private long testAverageJavaObjectReadAccessTime() throws IOException {
        SimpleModel model = new ObjectMapper().readValue(inputJson, SimpleModel.class);
        String value;
        final long start = System.nanoTime();
        for (int i = 0; i < testAttempts; i++) {
            value = model.getField6();
        }
        final long end = System.nanoTime();
        return (end - start)/testAttempts;
    }

    private long testAverageJsonObjectReadAccessTime() {
        JSONObject jsonObject = new JSONObject(inputJson);
        String value;
        final long start = System.nanoTime();
        for (int i = 0; i < testAttempts; i++) {
            value = jsonObject.getString("field6");
        }
        final long end = System.nanoTime();
        return (end - start)/testAttempts;
    }

    private long testAverageJsonObjectDeserializationTime() {
        JSONObject jsonObject;
        final long start = System.nanoTime();
        for (int i = 0; i < testAttempts; i++) {
            jsonObject = new JSONObject(inputJson);
        }
        final long end = System.nanoTime();
        return (end - start)/testAttempts;
    }

    private long testAverageJacksonDeserializationTime() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModel model;
        final long start = System.nanoTime();
        for (int i = 0; i < testAttempts; i++) {
            model = mapper.readValue(inputJson, SimpleModel.class);
        }
        final long end = System.nanoTime();
        return (end - start)/testAttempts;
    }
}
