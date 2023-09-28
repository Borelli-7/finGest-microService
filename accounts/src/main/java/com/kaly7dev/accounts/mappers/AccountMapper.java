package com.kaly7dev.accounts.mappers;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "accDTo_userID", expression = "java(account.getOwner().getUserID())")
    AccountDto mapToDto(Account account);

    @Mapping(target = "accDTo_userID", expression = "java(account.getOwner().getUserID())")
    AccountDtoSilm mapToDtoSlim(Account account);
}
