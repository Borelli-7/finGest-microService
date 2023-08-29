package com.kaly7dev.balances.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceDto(
        Long balanceID,
        String description,
        double price,
        Instant createdDate,
        boolean assets,
        BigDecimal amount
) {
}
