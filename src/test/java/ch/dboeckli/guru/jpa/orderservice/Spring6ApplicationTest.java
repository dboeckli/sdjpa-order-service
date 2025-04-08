package ch.dboeckli.guru.jpa.orderservice;

import ch.dboeckli.guru.jpa.orderservice.repository.OrderHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static ch.dboeckli.guru.jpa.orderservice.loader.TestDataLoader.ORDERS_TO_CREATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@Slf4j
class Spring6ApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OrderHeaderRepository orderHeaderRepository;

    @Test
    void contextLoads() {
        log.info("Testing Spring 6 Application {}", applicationContext.getApplicationName());
        assertAll(
            () -> assertNotNull(applicationContext, "Application context should not be null"),
            () -> assertThat(orderHeaderRepository.count()).isGreaterThanOrEqualTo(ORDERS_TO_CREATE)
        );
    }

}
