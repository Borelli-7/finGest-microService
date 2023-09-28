package com.kaly7dev.accounts.controllers;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountController {

    ResponseEntity<String>  initializeAccountsUser();
    ResponseEntity<List<AccountDtoSilm>> updateAccountListUser(AccountDtoSilm... accountDtoSlimList) throws AccountNotFoundException;
    ResponseEntity<List<AccountDto>> accountsListUser();
}
