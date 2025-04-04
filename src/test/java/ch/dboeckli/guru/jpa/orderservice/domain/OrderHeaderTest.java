package ch.dboeckli.guru.jpa.orderservice.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderHeaderTest {

    @Test
    void testEquals() {
        OrderHeader oh1 = new OrderHeader();
        oh1.setId(1L);

        OrderHeader oh2 = new OrderHeader();
        oh2.setId(1L);

        assert oh1.equals(oh2);
    }

    @Test
    void testNotEquals() {
        OrderHeader oh1 = new OrderHeader();
        oh1.setId(1L);

        OrderHeader oh2 = new OrderHeader();
        oh2.setId(3L);

        assertNotEquals(oh1, oh2);
    }

    @Test
    void testHashCode() {
        OrderHeader oh1 = new OrderHeader();
        oh1.setId(1L);

        OrderHeader oh2 = new OrderHeader();
        oh2.setId(1L);

        OrderHeader oh3 = new OrderHeader();
        oh3.setId(2L);

        // OrderHeaders with the same ID should have the same hash code
        assertEquals(oh1.hashCode(), oh2.hashCode());

        // OrderHeaders with different IDs should have different hash codes
        assertNotEquals(oh1.hashCode(), oh3.hashCode());

        // The hash code should be consistent
        int hashCode1 = oh1.hashCode();
        int hashCode2 = oh1.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

}