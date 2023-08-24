package com.kaly7dev.results.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record ResultDto (
         Long resultID,
         String description,
         BigDecimal amount,
         Instant createdDate,
         Instant updatedDate,
         boolean inFlow,
         int weekNumber,
         boolean fixedOutflow
){ }
