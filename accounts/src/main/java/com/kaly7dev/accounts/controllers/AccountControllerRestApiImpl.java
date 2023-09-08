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

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountControllerRestApiImpl implements AccountController {

    private final AccountService accountService;

    @Override
    @PutMapping("/updatelist")
    public ResponseEntity<List<AccountDtoSilm>> updateAccountList(
            @RequestBody AccountDtoSilm... accountDtoSlimList
    ) throws AccountNotFoundException {

        return status(OK)
                .body(accountService.updateAccountList(accountDtoSlimList));
    }

    @Override
    @GetMapping("/listall")
    public ResponseEntity<List<AccountDto>> accountsList() {

        return status(OK)
                .body(accountService.accountsList());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
