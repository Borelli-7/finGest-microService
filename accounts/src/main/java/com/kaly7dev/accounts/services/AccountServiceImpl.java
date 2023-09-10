package com.kaly7dev.accounts.services;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.entities.Account;
import com.kaly7dev.accounts.exceptions.AccountServiceException;
import com.kaly7dev.accounts.mappers.AccountMapper;
import com.kaly7dev.accounts.repositories.AccountRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    public static final String ACCOUNT_LIST_DB_SUCCESS= "Accounts list in DataBase successfully";

    @Override
    @Transactional
    public void initializeAccounts(AccountRepo accountRepo) {
        List<String> accountName=
                List.of("Finances", "Savings", "Trainings", "Basics", "Leisure", "Gifts");
        Account account= new Account();

        List<Account> accountBackupList= accountRepo.findAll();
        if (accountBackupList.isEmpty()) {
            initializeAccountList(accountRepo, accountName, account);

            log.info(ACCOUNT_LIST_DB_SUCCESS+" Initialized ! ");

        }else if (accountBackupList.size() == 6){
            restoreAccountList(accountBackupList);

        }else {
            throw new AccountServiceException("Account List Exist in DataBase, But The Size is Not Equals to 6 !");
        }
    }

    private static void restoreAccountList(List<Account> accountBackupList) {
        var sum= accountBackupList.stream()
                .mapToInt(Account::getPercentage).sum();

        if (sum == 100) {
            log.info(ACCOUNT_LIST_DB_SUCCESS+" Restored ! ");
        }else{
            throw new AccountServiceException(
                    "The 6 Accounts Exist In DataBase, But The Sum of Their Percentage NoT Equal to 100 !");
        }
    }

    private static void initializeAccountList(AccountRepo accountRepo, List<String> accountName, Account account) {
        for (String accN : accountName) {
            account.setAccountId(UUID.randomUUID().toString());
            account.setName(accN);
            int percent = switch (account.getName()) {
                case "Finances", "Trainings" -> 10;
                case "Leisure", "Gifts" -> 5;
                case "Savings" -> 15;
                case "Basics" -> 55;
                default -> throw new IllegalStateException("Unexpected Account name: " + account.getName());
            };
            account.setPercentage(percent);
            accountRepo.save(account);
        }
    }

    @Override
    @Transactional
    public List<AccountDtoSilm> updateAccountList(AccountDtoSilm... accountDtoSlimList) throws AccountNotFoundException {
        List<Account> accountsFromDB= accountRepo.findAll();

        checkIfAccountNameExistInDB(accountDtoSlimList);

        for (var accDtoSl :accountDtoSlimList) {
            for (var account : accountsFromDB) {
                if (account.getName().equals(accDtoSl.name())) {
                    account.setPercentage(accDtoSl.percentage());

                    break;
                }
            }
        }

        return checkTotalPercentageAccounts(accountsFromDB, accountDtoSlimList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> accountsList() {
        List<AccountDto> accountDtoList= new ArrayList<>();
        List<Account> accountList= accountRepo.findAll();

        accountList.forEach( acc -> accountDtoList.add( accountMapper.mapToDto(acc)));

        log.info(" All Accounts Successfully listed ! ");
        return accountDtoList;
    }

    private void checkIfAccountNameExistInDB(AccountDtoSilm[] accountDtoSlimList) throws AccountNotFoundException {
        for (var accDtoSl : accountDtoSlimList) {
            Optional<Account> accToEdit = accountRepo.findByName(accDtoSl.name());

            if (accToEdit.isEmpty()) {
                throw new AccountNotFoundException(
                        String.format( "Account With Name: %s Not Found !", accDtoSl.name()));
            }
            if(Objects.equals(accToEdit.orElseThrow().getPercentage(), accDtoSl.percentage())){
                throw new AccountServiceException(
                        String.format("%s Account already contains this percentage", accDtoSl.name()));
            }
        }
    }

    private List<AccountDtoSilm> checkTotalPercentageAccounts(
            List<Account> accountsFromDB,
            AccountDtoSilm[] accountDtoSlimList) {
        List<AccountDtoSilm> response= new ArrayList<>();

        var sum= accountsFromDB.stream()
                    .mapToInt(Account::getPercentage).sum();

        if (sum != 100) {
            throw new AccountServiceException("The sum of All Accounts percentage must be equal to 100 !");
        }else {
            for (var accDtoSl : accountDtoSlimList) {
                for (var account : accountsFromDB) {
                    if (account.getName().equals(accDtoSl.name())) {
                        account.setUpdatedDate(Instant.now());
                        response.add(accountMapper.mapToDtoSlim(account));
                        accountRepo.save(account);

                        log.info("{} Account Percentage Successfully Updated ! ", account.getName() );

                        break;
                    }
                }
            }
        }
        return response;
    }

}
