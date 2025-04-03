package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderHeader that)) return false;
        if (!super.equals(o)) return false;

        if (getCustomer() != null ? !getCustomer().equals(that.getCustomer()) : that.getCustomer() != null)
            return false;
        if (shippingAddress != null ? !shippingAddress.equals(that.shippingAddress) : that.shippingAddress != null)
            return false;
        return billToAddress != null ? billToAddress.equals(that.billToAddress) : that.billToAddress == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (shippingAddress != null ? shippingAddress.hashCode() : 0);
        result = 31 * result + (billToAddress != null ? billToAddress.hashCode() : 0);
        return result;
    }
}
