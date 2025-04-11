package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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

    @Size(max = 50)
    private String customerName;

    @Embedded
    @Valid
    private Address address;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    @Email
    private String email;

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<OrderHeader> orders = new LinkedHashSet<>();
}
