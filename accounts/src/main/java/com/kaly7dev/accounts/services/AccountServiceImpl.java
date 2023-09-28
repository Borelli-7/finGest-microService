package com.kaly7dev.accounts.services;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.entities.Account;
import com.kaly7dev.accounts.exceptions.AccountServiceException;
import com.kaly7dev.accounts.mappers.AccountMapper;
import com.kaly7dev.accounts.models.User;
import com.kaly7dev.accounts.repositories.AccountRepo;
import com.kaly7dev.accounts.repositories.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
@Primary
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final AccountMapper accountMapper;

    private static final String ACCOUNT_LIST_DB_SUCCESS= "Accounts list in DataBase successfully";

    private static final String ACC_USER_ID= "2";

    @Override
    @Transactional
    public void initializeAccountsUser() {
        List<String> accountNameList =
                List.of("Finances", "Savings", "Trainings", "Basics", "Leisure", "Gifts");
        Account account= new Account();
        User userChecked= checkUser(ACC_USER_ID);
        log.info(String.format("User: %s  Successfully Checked ", userChecked.getFirstName()));


        List<Account> accountsUser=accountRepo
                .findAllByOwner(userChecked);

        if (accountsUser.isEmpty()) {
            initializeAccountList(accountRepo, accountNameList, account, userChecked);
            log.info(String.format(ACCOUNT_LIST_DB_SUCCESS + " Initialized for User: %s !", userChecked.getFirstName()));
        } else {
            checkTotalPercentageAccountsUserFromDB(accountsUser, userChecked);
        }
    }
    private void checkTotalPercentageAccountsUserFromDB(
            List<Account> accountsFromDB,
            User owner) {

        var sum= accountsFromDB.stream()
                .mapToInt(Account::getPercentage).sum();

        /*
         * the sum of the percentages of the 6 accounts must equal 100.
         * in order to respect the principle of the 6-account strategy in money management.
         */
        if (sum != 100) {
            throw new AccountServiceException(
                    String.format("The sum of All Accounts percentage for User: %S" +
                            " is Not equal to 100 !",owner.getFirstName()));
        }else {
            log.info("User: {}'s accounts have already been initialized !", owner.getFirstName());
        }
    }

    private User checkUser(String userId) {
        return userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException(
                        String.format("User ID: %s Not Exist In DataBase ! ",userId)));
    }

    private static void initializeAccountList(AccountRepo accountRepo,
                                              List<String> accountName,
                                              Account account,
                                              User owner) {

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
            account.setOwner(owner);

            accountRepo.save(account);
        }
    }

    @Override
    @Transactional
    public List<AccountDtoSilm> updateAccountListUser(AccountDtoSilm...accountDtoSlimList) throws AccountNotFoundException {
        User userChecked= checkUser(ACC_USER_ID);
        List<Account> accountsUserFromDB = accountRepo.findAllByOwner(userChecked);

        checkIfAccountNameExistInDB(accountDtoSlimList, userChecked);

        for (var accDtoSl :accountDtoSlimList) {
            for (var account : accountsUserFromDB) {
                if (account.getName().equals(accDtoSl.name())) {
                    account.setPercentage(accDtoSl.percentage());

                    break;
                }
            }
        }

        return checkTotalPercentageAccountsUserFromClient(accountsUserFromDB, accountDtoSlimList, userChecked);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> accountsListUser() {
        User userChecked= checkUser(ACC_USER_ID);

        List<AccountDto> accountDtoList= new ArrayList<>();
        List<Account> accountList= accountRepo.findAllByOwner(userChecked);


        accountList.forEach( acc -> accountDtoList.add( accountMapper.mapToDto(acc)));

        log.info(" All Accounts Successfully listed ! ");
        return accountDtoList;
    }

    private void checkIfAccountNameExistInDB(
            AccountDtoSilm[] accountDtoSlimList,
            User userChecked) throws AccountNotFoundException {

        for (var accDtoSl : accountDtoSlimList) {
            Optional<Account> accToEdit = accountRepo.findByNameAndOwner_UserID(
                    accDtoSl.name(), userChecked.getUserID());

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

    private List<AccountDtoSilm> checkTotalPercentageAccountsUserFromClient(
            List<Account> accountsFromDB,
            AccountDtoSilm[] accountDtoSlimList,
            User userChecked) {
        List<AccountDtoSilm> response= new ArrayList<>();

        var sum= accountsFromDB.stream()
                    .mapToInt(Account::getPercentage).sum();

        /*
         * the sum of the percentages of the 6 accounts must equal 100.
         * in order to respect the principle of the 6-account strategy in money management.
         */
        if (sum != 100) {
            throw new AccountServiceException(
                    String.format("The sum of All Accounts percentage for User: %s" +
                            " must be equal to 100 !", userChecked.getFirstName()));
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
