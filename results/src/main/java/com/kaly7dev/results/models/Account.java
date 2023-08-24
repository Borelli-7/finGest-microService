package com.kaly7dev.results.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class Account {
    private String accountId;
    private String name;
    private Integer percentage;
    private Instant updatedDate;
    private BigDecimal debitedAmount;
    private BigDecimal creditedAmount;
    private Integer monthNumber;
}
