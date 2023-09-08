package com.kaly7dev.accounts.mappers;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.entities.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto mapToDto(Account account);

    AccountDtoSilm mapToDtoSlim(Account account);
}
