package ch.dboeckli.guru.jpa.orderservice.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testEquals() {
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(1L);

        assert product1.equals(product2);
    }

    @Test
    void testNotEquals() {
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(3L);

        assertNotEquals(product1, product2);
    }

    @Test
    void testHashCode() {
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(1L);

        Product product3 = new Product();
        product3.setId(2L);

        // Products with the same ID should have the same hash code
        assertEquals(product1.hashCode(), product2.hashCode());

        // Products with different IDs should have different hash codes
        assertNotEquals(product1.hashCode(), product3.hashCode());

        // The hash code should be consistent
        int hashCode1 = product1.hashCode();
        int hashCode2 = product1.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}