package com.kaly7dev.accounts.services;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountService {

    void initializeAccountsUser();

    List<AccountDtoSilm> updateAccountListUser( AccountDtoSilm... accountDtoSlimList) throws AccountNotFoundException;

    List<AccountDto> accountsListUser();
}
