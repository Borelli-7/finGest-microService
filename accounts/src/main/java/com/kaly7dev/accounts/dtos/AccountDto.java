package com.kaly7dev.accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountDto(
         String accountId,
         String name,
         Integer percentage,
         Instant updatedDate,
         BigDecimal debitedAmount,
         BigDecimal creditedAmount,
         Integer monthNumber,
         String accDTo_userID
) {
}
