package com.kaly7dev.balances.mappers;

import com.kaly7dev.balances.dtos.BalanceDto;
import com.kaly7dev.balances.entities.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceMapper {
    @Mapping(target = "balanceID", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Balance mapToBalance(BalanceDto balanceDto);
    BalanceDto mapToDto(Balance balance);
}
