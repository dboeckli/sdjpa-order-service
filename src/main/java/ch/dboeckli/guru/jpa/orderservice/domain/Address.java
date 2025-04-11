package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class Address {

    @Size(max = 30)
    private String address;

    @Size(max = 30)
    private String city;

    @Size(max = 30)
    private String state;

    @Size(max = 30)
    private String zipCode;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Address address1 = (Address) o;
        return getAddress() != null && Objects.equals(getAddress(), address1.getAddress())
            && getCity() != null && Objects.equals(getCity(), address1.getCity())
            && getState() != null && Objects.equals(getState(), address1.getState())
            && getZipCode() != null && Objects.equals(getZipCode(), address1.getZipCode());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(address,
            city,
            state,
            zipCode);
    }
}
