package ch.dboeckli.guru.jpa.orderservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address1;
    private Address address2;
    private Address address3;

    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setAddress("123 Main St");
        address1.setCity("Springfield");
        address1.setState("IL");
        address1.setZipCode("62701");

        address2 = new Address();
        address2.setAddress("123 Main St");
        address2.setCity("Springfield");
        address2.setState("IL");
        address2.setZipCode("62701");

        address3 = new Address();
        address3.setAddress("456 Elm St");
        address3.setCity("Springfield");
        address3.setState("IL");
        address3.setZipCode("62701");
    }

    @Test
    void testEquals() {
        assertAll(
            () -> assertEquals(address1, address2, "Equal addresses should be equal"),
            () -> assertNotEquals(address1, address3, "Different addresses should not be equal"),
            () -> assertNotEquals(null, address1, "Address should not be equal to null"),
            () -> assertEquals(address1, address1, "Address should be equal to itself"),
            () -> assertAll("Symmetry test",
                () -> assertEquals(address1, address2),
                () -> assertEquals(address2, address1)
            ),
            () -> assertNotEquals(new Object(), address1, "Address should not be equal to object of different type")
        );
    }

    @Test
    void testHashCode() {
        int initialHashCode = address1.hashCode();
        assertAll(
            () -> assertEquals(address1.hashCode(), address2.hashCode(), "Equal addresses should have the same hash code"),
            () -> assertNotEquals(address1.hashCode(), address3.hashCode(), "Different addresses should have different hash codes"),
            () -> assertEquals(initialHashCode, address1.hashCode(), "Hash code should be consistent"),
            () -> assertEquals(initialHashCode, address1.hashCode(), "Hash code should be consistent on multiple calls")
        );
    }
}