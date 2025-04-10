package ch.dboeckli.guru.jpa.orderservice.repository.mysql;

import ch.dboeckli.guru.jpa.orderservice.domain.Customer;
import ch.dboeckli.guru.jpa.orderservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test_mysql")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class CustomerRepositoryIT {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testCreateNewCustomer() {
        Customer customer1 = new Customer();
        customer1.setCustomerName("Testing Version");
        Customer savedCustomer1 = customerRepository.save(customer1);

        assertThat(savedCustomer1.getVersion()).isGreaterThanOrEqualTo(0);
    }


}