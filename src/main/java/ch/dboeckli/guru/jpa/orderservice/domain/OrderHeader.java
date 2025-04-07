package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@AttributeOverrides({
    @AttributeOverride(
        name = "shippingAddress.address",
        column = @Column(name = "shipping_address")
    ),
    @AttributeOverride(
        name = "shippingAddress.city",
        column = @Column(name = "shipping_city")
    ),
    @AttributeOverride(
        name = "shippingAddress.state",
        column = @Column(name = "shipping_state")
    ),
    @AttributeOverride(
        name = "shippingAddress.zipCode",
        column = @Column(name = "shipping_zip_code")
    ),
    @AttributeOverride(
        name = "billToAddress.address",
        column = @Column(name = "bill_to_address")
    ),
    @AttributeOverride(
        name = "billToAddress.city",
        column = @Column(name = "bill_to_city")
    ),
    @AttributeOverride(
        name = "billToAddress.state",
        column = @Column(name = "bill_to_state")
    ),
    @AttributeOverride(
        name = "billToAddress.zipCode",
        column = @Column(name = "bill_to_zip_code")
    )
})
public class OrderHeader extends BaseEntity {

    @ManyToOne
    private Customer customer;

    @Embedded
    private Address shippingAddress;

    @Embedded
    private Address billToAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orderHeader", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @ToString.Exclude
    private Set<OrderLine> orderLines;

    @OneToOne(cascade = CascadeType.PERSIST)
    private OrderApproval orderApproval;

    public void addOrderLine(OrderLine orderLine) {
        if (orderLines == null) {
            orderLines = new HashSet<>();
        }
        orderLines.add(orderLine);
        orderLine.setOrderHeader(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderHeader that)) return false;
        if (!super.equals(o)) return false;

        if (getCustomer() != null ? !getCustomer().equals(that.getCustomer()) : that.getCustomer() != null)
            return false;
        if (getShippingAddress() != null ? !getShippingAddress().equals(that.getShippingAddress()) : that.getShippingAddress() != null)
            return false;
        if (getBillToAddress() != null ? !getBillToAddress().equals(that.getBillToAddress()) : that.getBillToAddress() != null)
            return false;
        if (getOrderStatus() != that.getOrderStatus()) return false;
        return getOrderLines() != null ? getOrderLines().equals(that.getOrderLines()) : that.getOrderLines() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (getShippingAddress() != null ? getShippingAddress().hashCode() : 0);
        result = 31 * result + (getBillToAddress() != null ? getBillToAddress().hashCode() : 0);
        result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
        result = 31 * result + (getOrderLines() != null ? getOrderLines().hashCode() : 0);
        return result;
    }
}
