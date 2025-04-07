package ch.dboeckli.guru.jpa.orderservice.repository;

import ch.dboeckli.guru.jpa.orderservice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
