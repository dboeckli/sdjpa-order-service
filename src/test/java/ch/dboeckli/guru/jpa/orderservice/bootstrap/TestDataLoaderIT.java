package ch.dboeckli.guru.jpa.orderservice.bootstrap;

import ch.dboeckli.guru.jpa.orderservice.domain.Address;
import ch.dboeckli.guru.jpa.orderservice.domain.Customer;
import ch.dboeckli.guru.jpa.orderservice.domain.OrderHeader;
import ch.dboeckli.guru.jpa.orderservice.repository.CustomerRepository;
import ch.dboeckli.guru.jpa.orderservice.repository.OrderHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static ch.dboeckli.guru.jpa.orderservice.bootstrap.TestDataLoader.CUSTOMER_NAME_DEMO;
import static ch.dboeckli.guru.jpa.orderservice.bootstrap.TestDataLoader.ORDERS_TO_CREATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@ActiveProfiles("test_mysql")
@Slf4j
class TestDataLoaderIT {

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testDataLoader() {
        assertThat(orderHeaderRepository.count()).isGreaterThanOrEqualTo(ORDERS_TO_CREATE);
    }

    @Test
    @Transactional // this way all changes are rolled back after the test. with @DataJpaTest it's enabled by default
    void testDBLock() {
        log.info("### Starting test with DB lock");
        Long id = 55L;

        OrderHeader orderHeader = orderHeaderRepository.findById(id).get();

        Address billTo = new Address();
        billTo.setAddress("Bill me");
        orderHeader.setBillToAddress(billTo);
        orderHeaderRepository.saveAndFlush(orderHeader);

        log.info("### I updated the order");
    }

    @Test
    // Remove @Transactional to ensure lazy loading exception occurs
    void testLazyLoading() {
        log.info("### Starting test for Lazy Loading");
        Optional<Customer> customer = customerRepository.findCustomerByCustomerNameIgnoreCase(CUSTOMER_NAME_DEMO);
        List<OrderHeader> orders = orderHeaderRepository.findAllByCustomer(customer.get());

        OrderHeader orderHeader = orderHeaderRepository.findById(orders.getFirst().getId()).orElse(null);

        assertThrows(LazyInitializationException.class, () -> orderHeader.getOrderLines().forEach(orderLine -> {
            log.info("### Product Description: {}", orderLine.getProduct().getDescription());

            // This should throw LazyInitializationException
            orderLine.getProduct().getCategories().forEach(category -> log.info("### Category: {}", category.getDescription()));
        }));
        log.info("### LazyInitializationException was thrown as expected");
    }

    @Test
    @Transactional
    void testLazyLoadingWithTransactional() {
        log.info("### Starting test for Lazy Loading");
        Optional<Customer> customer = customerRepository.findCustomerByCustomerNameIgnoreCase(CUSTOMER_NAME_DEMO);
        List<OrderHeader> orders = orderHeaderRepository.findAllByCustomer(customer.get());

        OrderHeader orderHeader = orderHeaderRepository.findById(orders.getFirst().getId()).orElse(null);

        orderHeader.getOrderLines().forEach(orderLine -> {
            log.info("### Product Description: {}", orderLine.getProduct().getDescription());

            // This should throw LazyInitializationException
            orderLine.getProduct().getCategories().forEach(category -> log.info("### Category: {}", category.getDescription()));
        });
        log.info("### No LazyInitializationException was thrown as expected");
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("Testing Version");
        Customer savedCustomer = customerRepository.save(customer);
        assertThat(savedCustomer.getVersion()).isGreaterThanOrEqualTo(0);
        log.info("### Version is: " + savedCustomer.getVersion());

        savedCustomer.setCustomerName("Testing Version 2");
        Customer savedCustomer2 = customerRepository.save(customer);
        assertThat(savedCustomer2.getVersion()).isGreaterThanOrEqualTo(1);
        log.info("### Version is: " + savedCustomer2.getVersion());

        savedCustomer2.setCustomerName("Testing Version 3");
        Customer savedCustomer3 = customerRepository.save(savedCustomer2);
        assertThat(savedCustomer3.getVersion()).isGreaterThanOrEqualTo(2);
        log.info("### Version is: " + savedCustomer3.getVersion());
    }
}