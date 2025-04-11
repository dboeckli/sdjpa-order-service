package ch.dboeckli.guru.jpa.orderservice.repository.mysql;

import ch.dboeckli.guru.jpa.orderservice.domain.Address;
import ch.dboeckli.guru.jpa.orderservice.domain.Customer;
import ch.dboeckli.guru.jpa.orderservice.repository.CustomerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test_mysql")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class CustomerRepositoryIT {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testCreateNewCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("Testing Version");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer.getVersion()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void testSaveOrderCustomerNameTooLong() {
        Customer customer = new Customer();
        customer.setCustomerName("New Customer012345678901234567890123456789012345678901");
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.save(customer));
        assertAll("Customer Name Too Long Validation",
            () -> assertEquals(1, exception.getConstraintViolations().size()),
            () -> assertEquals("customerName", exception.getConstraintViolations().iterator().next().getPropertyPath().toString()),
            () -> assertEquals("size must be between 0 and 50", exception.getConstraintViolations().iterator().next().getMessage())
        );
    }

    @Test
    void testSaveOrderCustomerNameTooLongAndPhoneTooLong() {
        Customer customer = new Customer();
        customer.setCustomerName("New Customer012345678901234567890123456789012345678901");
        customer.setPhone("12345678901234567890123456789012345678");
        customer.setEmail("test.example.com");
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.save(customer));

        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        violations.forEach(violation -> {
            log.info("Violation on property: {}", violation.getPropertyPath());
            log.info("Invalid value: {}", violation.getInvalidValue());
            log.info("Error message: {}", violation.getMessage());
            log.info("--------------------");
        });

        assertAll("Customer Name and Phone Too Long Validation",
            () -> assertEquals(3, violations.size()),

            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("customerName") &&
                    violation.getMessage().equals("size must be between 0 and 50")
            )),
            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("phone") &&
                    violation.getMessage().equals("size must be between 0 and 20")
            )),
            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("email") &&
                    violation.getMessage().equals("must be a well-formed email address")
            ))
        );
    }

    @Test
    void testSaveOrderCustomerInvalidAddress() {
        Customer customer = new Customer();

        Address address = new Address();
        address.setAddress("123 Main012345678901234567890123456789012345678901012345678901234567890123456789012345678901012345678901234567890123456789012345678901");
        address.setCity("New Orleans012345678901234567890123456789012345678901012345678901234567890123456789012345678901012345678901234567890123456789012345678901");
        address.setState("LA012345678901234567890123456789012345678901012345678901234567890123456789012345678901012345678901234567890123456789012345678901");
        address.setZipCode("870312345678901234567890123456789012345678901012345678901234567890123456789012345678901012345678901234567890123456789012345678901");
        customer.setAddress(address);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.save(customer));

        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(violation -> {
            log.info("Violation on property: {}", violation.getPropertyPath());
            log.info("Invalid value: {}", violation.getInvalidValue());
            log.info("Error message: {}", violation.getMessage());
            log.info("--------------------");
        });

        assertAll("Address Validation",
            () -> assertEquals(4, violations.size()),

            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("address.address") &&
                    violation.getMessage().equals("size must be between 0 and 30")
            )),
            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("address.city") &&
                    violation.getMessage().equals("size must be between 0 and 30")
            )),
            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("address.state") &&
                    violation.getMessage().equals("size must be between 0 and 30")
            )),
            () -> assertTrue(violations.stream().anyMatch(violation ->
                violation.getPropertyPath().toString().equals("address.zipCode") &&
                    violation.getMessage().equals("size must be between 0 and 30")
            ))
        );
    }

    @Test
    void equalsAndHashCodeTestWithProxy() {
        Customer saved = customerRepository.save(new Customer());

        Customer customer = customerRepository.findById(saved.getId()).orElseThrow();
        Customer customerProxy = customerRepository.getReferenceById(saved.getId());

        assertEquals(customer, customerProxy);
        assertEquals(customerProxy, customer);
        assertEquals(customer.hashCode(), customerProxy.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentIds() {
        Customer customer1 = customerRepository.save(new Customer());
        Customer customer2 = customerRepository.save(new Customer());

        assertNotEquals(customer1, customer2);
    }

    @Test
    void testEqualsWithNullAndOtherClass() {
        Customer customer = customerRepository.save(new Customer());

        assertNotEquals(null, customer);
        assertNotEquals(new Object(), customer);
    }

    @Test
    void testHashCodeConsistency() {
        Customer customer = customerRepository.save(new Customer());
        int hashCode1 = customer.hashCode();
        int hashCode2 = customer.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}