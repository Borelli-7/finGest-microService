package com.kaly7dev.accounts.controllers;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountControllerRestApiImpl implements AccountController {

    private final AccountService accountService;

    @Override
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeAccountsUser() {
        accountService.initializeAccountsUser();
        return new ResponseEntity<>(CREATED);
    }

    @Override
    @PutMapping("/updatelist")
    public ResponseEntity<List<AccountDtoSilm>> updateAccountListUser(
            @RequestBody AccountDtoSilm... accountDtoSlimList
    ) throws AccountNotFoundException {

        return status(OK)
                .body(accountService.updateAccountListUser(accountDtoSlimList));
    }

    @Override
    @GetMapping("/listall")
    public ResponseEntity<List<AccountDto>> accountsListUser() {

        return status(OK)
                .body(accountService.accountsListUser());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
