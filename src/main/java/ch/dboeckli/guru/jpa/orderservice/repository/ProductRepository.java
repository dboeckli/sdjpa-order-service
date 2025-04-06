package ch.dboeckli.guru.jpa.orderservice.repository;

import ch.dboeckli.guru.jpa.orderservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>  {

    Product findByDescription(String description);

}
