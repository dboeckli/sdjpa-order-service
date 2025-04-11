package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderLine extends BaseEntity {

    @Version
    private Integer version;

    private Integer quantityOrdered;

    @ManyToOne
    @ToString.Exclude
    private OrderHeader orderHeader;

    @ManyToOne
    private Product product;
}
