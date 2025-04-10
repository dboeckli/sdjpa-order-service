package ch.dboeckli.guru.jpa.orderservice.bootstrap;

import ch.dboeckli.guru.jpa.orderservice.domain.OrderHeader;
import ch.dboeckli.guru.jpa.orderservice.repository.OrderHeaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BootstrapOrderService {

    private final OrderHeaderRepository orderHeaderRepository;

    @Transactional
    public void demonstrateLazyLoading(Long singleOrderHeaderId) {
        OrderHeader orderHeader = orderHeaderRepository.findById(singleOrderHeaderId).orElse(null);
        orderHeader.getOrderLines().forEach(orderLine -> {
            log.info("### Product Description: {}", orderLine.getProduct().getDescription());

            // here we get a lazy loading initializing exception
            try {
                orderLine.getProduct().getCategories().forEach(category -> log.info("### Category: {}", category.getDescription()));
                log.error("### No LazyInitializationException has been thrown");
            } catch (LazyInitializationException ex) {
                log.error("### Expected LazyInitializationException for demonstration", ex);
            }
        });
    }

}
