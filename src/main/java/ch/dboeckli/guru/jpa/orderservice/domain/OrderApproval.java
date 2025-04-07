package ch.dboeckli.guru.jpa.orderservice.domain;

import jakarta.persistence.Entity;
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

}
