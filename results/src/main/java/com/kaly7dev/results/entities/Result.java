package com.kaly7dev.results.entities;

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
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long resultID;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal amount;
    private Instant createdDate;
    private Instant updatedDate;
    @Column(nullable = false)
    private boolean inFlow;
    private int weekNumber;
    @Column(nullable = false)
    private boolean fixedOutflow;
}
