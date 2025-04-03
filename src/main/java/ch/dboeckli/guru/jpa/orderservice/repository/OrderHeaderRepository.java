package ch.dboeckli.guru.jpa.orderservice.repository;

import ch.dboeckli.guru.jpa.orderservice.domain.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
}
