package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer extends BaseEntity {

    @Version
    private Integer version;

    private String customerName;

    @Embedded
    private Address address;

    private String phone;
    private String email;

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<OrderHeader> orders = new LinkedHashSet<>();

}
