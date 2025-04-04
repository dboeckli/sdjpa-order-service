package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product extends BaseEntity {

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(description, product.description) && productStatus == product.productStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, productStatus);
    }
}
