package org.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProductAppTest {

    @Test
    public void testHashRecord() {
        // Given a sample input record
        String inputRecord = "Role: Manager, Product: Test Product, Price: 99.99";

        // When we hash the record
        String hashedOutput = ProductApp.hashRecord(inputRecord);

        // Expected SHA-256 hash for the input record
        String expectedHash = "6ca74835a51d5db1a7108857cdb103842fbb4c853ec49112457c7c861b1e7755";

        // Then the output should match the expected hash
        assertEquals(expectedHash, hashedOutput);
    }
}
