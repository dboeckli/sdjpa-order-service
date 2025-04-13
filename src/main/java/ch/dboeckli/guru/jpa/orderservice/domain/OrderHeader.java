package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@AttributeOverride(
    name = "shippingAddress.address",
    column = @Column(name = "shipping_address")
)
@AttributeOverride(
    name = "shippingAddress.city",
    column = @Column(name = "shipping_city")
)
@AttributeOverride(
    name = "shippingAddress.state",
    column = @Column(name = "shipping_state")
)
@AttributeOverride(
    name = "shippingAddress.zipCode",
    column = @Column(name = "shipping_zip_code")
)
@AttributeOverride(
    name = "billToAddress.address",
    column = @Column(name = "bill_to_address")
)
@AttributeOverride(
    name = "billToAddress.city",
    column = @Column(name = "bill_to_city")
)
@AttributeOverride(
    name = "billToAddress.state",
    column = @Column(name = "bill_to_state")
)
@AttributeOverride(
    name = "billToAddress.zipCode",
    column = @Column(name = "bill_to_zip_code")
)
public class OrderHeader extends BaseEntity {

    @Version
    private Integer version;

    @ManyToOne
    private Customer customer;

    @Embedded
    private Address shippingAddress;

    @Embedded
    private Address billToAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // For Performance Tuning: OrderLines should be fetched in a separate query to avoid N+1 problem
    @OneToMany(mappedBy = "orderHeader", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private Set<OrderLine> orderLines;

    // For Performance Tuning: OrderApproval should be fetched in a separate query to avoid N+1 problem
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Fetch(FetchMode.SELECT)
    @ToString.Exclude
    private OrderApproval orderApproval;

    public void setOrderApproval(OrderApproval orderApproval) {
        this.orderApproval = orderApproval;
        orderApproval.setOrderHeader(this);
    }

    public void addOrderLine(OrderLine orderLine) {
        if (orderLines == null) {
            orderLines = new HashSet<>();
        }
        orderLines.add(orderLine);
        orderLine.setOrderHeader(this);
    }
}
