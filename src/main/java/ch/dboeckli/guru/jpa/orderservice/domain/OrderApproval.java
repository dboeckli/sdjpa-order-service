package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderApproval extends BaseEntity {

    private String approvedBy;

    @OneToOne
    @JoinColumn(name = "order_header_id")
    @ToString.Exclude
    private OrderHeader orderHeader;

}
