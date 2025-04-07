package ch.dboeckli.guru.jpa.orderservice.repository;

import ch.dboeckli.guru.jpa.orderservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
