package ch.dboeckli.guru.jpa.orderservice.repository;

import ch.dboeckli.guru.jpa.orderservice.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>  {

    Optional<Product> findByDescription(String description);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NonNull
    Optional<Product> findById(@NonNull Long id);
}
