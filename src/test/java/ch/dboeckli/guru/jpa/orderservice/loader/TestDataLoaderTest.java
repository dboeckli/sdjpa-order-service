package ch.dboeckli.guru.jpa.orderservice.loader;

import ch.dboeckli.guru.jpa.orderservice.repository.CustomerRepository;
import ch.dboeckli.guru.jpa.orderservice.repository.OrderHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ch.dboeckli.guru.jpa.orderservice.loader.TestDataLoader.ORDERS_TO_CREATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@Slf4j
class TestDataLoaderTest {

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testDataLoader() {
        assertThat(orderHeaderRepository.count()).isGreaterThanOrEqualTo(ORDERS_TO_CREATE);
    }
}