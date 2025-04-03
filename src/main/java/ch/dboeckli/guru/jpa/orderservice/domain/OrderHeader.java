package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

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
public class OrderHeader extends BaseEntity  {

    private String customer;

    @Embedded
    private Address shippingAddress;

    @Embedded
    private Address billToAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderHeader that = (OrderHeader) o;
        return Objects.equals(customer, that.customer) && Objects.equals(shippingAddress, that.shippingAddress) && Objects.equals(billToAddress, that.billToAddress) && orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customer, shippingAddress, billToAddress, orderStatus);
    }
}
