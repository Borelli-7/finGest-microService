package com.kaly7dev.accounts.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
@Document(value = "accounts")
//@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    private String accountId;
    private String name;
    private Integer percentage;
    private Instant updatedDate;
    private BigDecimal debitedAmount;
    private BigDecimal creditedAmount;
    private Integer monthNumber;
}
