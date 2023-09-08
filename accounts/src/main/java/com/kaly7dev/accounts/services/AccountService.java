package com.kaly7dev.accounts.services;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.repositories.AccountRepo;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountService {

    void initializeAccounts(AccountRepo accountRepo);
    List<AccountDtoSilm> updateAccountList(AccountDtoSilm... accountDtoSlimList) throws AccountNotFoundException;

    List<AccountDto> accountsList();
}
