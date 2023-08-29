package com.kaly7dev.balances.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Results")
@Builder
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long balanceID;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double price;
    private Instant createdDate;
    @Column(nullable = false)
    private boolean assets;
    @Column(nullable = false)
    private BigDecimal amount;

}
