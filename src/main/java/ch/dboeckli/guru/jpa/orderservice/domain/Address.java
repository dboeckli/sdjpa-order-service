package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

    private String address;
    private String city;
    private String state;
    private String zipCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address1)) return false;

        if (getAddress() != null ? !getAddress().equals(address1.getAddress()) : address1.getAddress() != null)
            return false;
        if (getCity() != null ? !getCity().equals(address1.getCity()) : address1.getCity() != null) return false;
        if (getState() != null ? !getState().equals(address1.getState()) : address1.getState() != null) return false;
        return getZipCode() != null ? getZipCode().equals(address1.getZipCode()) : address1.getZipCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getAddress() != null ? getAddress().hashCode() : 0;
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getZipCode() != null ? getZipCode().hashCode() : 0);
        return result;
    }
}
