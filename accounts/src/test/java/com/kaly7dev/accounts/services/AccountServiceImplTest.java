package com.kaly7dev.accounts.services;

import com.kaly7dev.accounts.dtos.AccountDto;
import com.kaly7dev.accounts.dtos.AccountDtoSilm;
import com.kaly7dev.accounts.entities.Account;
import com.kaly7dev.accounts.exceptions.AccountServiceException;
import com.kaly7dev.accounts.mappers.AccountMapper;
import com.kaly7dev.accounts.repositories.AccountRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private AccountService accountService;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountRepo  accountRepo;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @BeforeEach
    void setup(){
        accountService= new AccountServiceImpl(accountRepo, accountMapper);
    }
    @Test
    @DisplayName("Should Create And Initialize All 6 Accounts If Not Exist In DataBase ! ")
    void initializeAccountsTest1() {
        List<Account> AccountListEmpty= new ArrayList<>();

        Mockito.when(accountRepo.findAll())
                .thenReturn(AccountListEmpty);

        accountService.initializeAccounts(accountRepo);
        Mockito.verify(accountRepo, Mockito.times(6))
                .save(accountArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should Restore Account List From DataBase IF Sum Equal 6 And Percentage Sum Equal 100 ! ")
    void initializeAccountsTest2() {
        Account account1= new Account("1", "Finances", 10, null, null, null, 0);
        Account account2= new Account("2", "Savings", 15, null, null, null, 0);
        Account account3= new Account("2", "Trainings", 10, null, null, null, 0);
        Account account4= new Account("3", "Basics", 55, null, null, null, 0);
        Account account5= new Account("4", "Leisure", 5, null, null, null, 0);
        Account account6= new Account("5", "Gifts", 5, null, null, null, 0);

        List<Account> expectedAccountList= new ArrayList<>();
        expectedAccountList.add(account1);
        expectedAccountList.add(account2);
        expectedAccountList.add(account3);
        expectedAccountList.add(account4);
        expectedAccountList.add(account5);
        expectedAccountList.add(account6);

        Mockito.when(accountRepo.findAll())
                .thenReturn(expectedAccountList);

        accountService.initializeAccounts(accountRepo);

        Assertions.assertThat(expectedAccountList).hasSize(6);
        Assertions.assertThat(expectedAccountList.stream().mapToInt(Account::getPercentage).sum()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should Throw Exception If Percentage Sum Not Equal 100 !")
    void initializeAccountsTest3() {

        List<Account> expectedAccountList= List.of(
                new Account("1", "Finances", 15, null, null, null, 0),
                new Account("2", "Savings", 15, null, null, null, 0),
                new Account("2", "Trainings", 10, null, null, null, 0),
                new Account("3", "Basics", 55, null, null, null, 0),
                new Account("4", "Leisure", 5, null, null, null, 0),
                new Account("5", "Gifts", 5, null, null, null, 0)
        );

        Mockito.when(accountRepo.findAll())
                .thenReturn(expectedAccountList);

        Assertions.assertThatThrownBy(
                ()-> accountService.initializeAccounts(accountRepo)
        ).isInstanceOf(AccountServiceException.class)
                .hasMessage("The 6 Accounts Exist In DataBase, But The Sum of Their Percentage NoT Equal to 100 !");
    }

    @Test
    @DisplayName("Should Throw Exception If Account list Size From DataBase Is Not Equal 6 !")
    void initializeAccountsTest4() {
        List<Account> expectedAccountList= List.of(
                new Account("1", "Finances", 15, null, null, null, 0),
                new Account("2", "Savings", 15, null, null, null, 0),
                new Account("2", "Trainings", 10, null, null, null, 0),
                new Account("3", "Basics", 55, null, null, null, 0),
                new Account("5", "Gifts", 5, null, null, null, 0)
        );

        Mockito.when(accountRepo.findAll())
                .thenReturn(expectedAccountList);

        Assertions.assertThatThrownBy(
                        ()-> accountService.initializeAccounts(accountRepo)
                ).isInstanceOf(AccountServiceException.class)
                .hasMessage("Account List Exist in DataBase, But The Size is Not Equals to 6 !");
    }
    @Test
    @DisplayName("Should Throw Exception If Name Account Not Exist In DataBase !")
    void updateAccountListTest1() {
        AccountDtoSilm accountDtoSilm1= new AccountDtoSilm("Save", 10, null);

        Optional<Account> accountOptional= Optional.empty();

        Mockito.when(accountRepo.findByName(accountDtoSilm1.name()))
                .thenReturn(accountOptional);

        Assertions.assertThatThrownBy(
                ()-> accountService.updateAccountList(accountDtoSilm1)
        ).isInstanceOf(AccountNotFoundException.class)
                .hasMessage("Account With Name: Save Not Found !");
    }

    @Test
    @DisplayName("Should Throw Exception If percentage Is Not Different ! ")
    void updateAccountListTest2() {
        AccountDtoSilm accountDtoSilm1= new AccountDtoSilm("Finances", 10, null);
        Account account1= new Account("1", "Finances", 10, null, null, null, 0);

        Optional<Account> accountOptional= Optional.of(account1);

        Mockito.when(accountRepo.findByName(accountDtoSilm1.name()))
                .thenReturn(accountOptional);

        Assertions.assertThatThrownBy(
                        ()-> accountService.updateAccountList(accountDtoSilm1)
                ).isInstanceOf(AccountServiceException.class)
                .hasMessage("Finances Account already contains this percentage");
    }

    @Test
    @DisplayName("Should Return Updated Account list ! ")
    void updateAccountListTest3() throws AccountNotFoundException {
        AccountDtoSilm accountDtoSilm1= new AccountDtoSilm("Finances", 10, null);
        AccountDtoSilm accountDtoSilm2= new AccountDtoSilm("Savings", 15, null);

        Account account1= new Account("1", "Finances", 15, null, null, null, 0);
        Account account2= new Account("2", "Savings", 10, null, null, null, 0);


        List<Account> expectedAccountList= List.of(
                new Account("1", "Finances", 10, null, null, null, 0),
                new Account("2", "Savings", 15, null, null, null, 0),
                new Account("2", "Trainings", 10, null, null, null, 0),
                new Account("3", "Basics", 55, null, null, null, 0),
                new Account("4", "Leisure", 5, null, null, null, 0),
                new Account("5", "Gifts", 5, null, null, null, 0)
        );

        Optional<Account> accountOptional1= Optional.of(account1);
        Optional<Account> accountOptional2= Optional.of(account2);

        Mockito.when(accountRepo.findAll())
                .thenReturn(expectedAccountList);

        Mockito.when(accountRepo.findByName(accountDtoSilm1.name()))
                .thenReturn(accountOptional1);
        Mockito.when(accountRepo.findByName(accountDtoSilm2.name()))
                .thenReturn(accountOptional2);

        accountService.updateAccountList(accountDtoSilm1, accountDtoSilm2);
        Mockito.verify(accountRepo, Mockito.times(2))
                .save(accountArgumentCaptor.capture());

    }

    @Test
    void accountsList() {
        Account account1= new Account("1", "Finances", 15, null, null, null, 0);
        AccountDto accountDto1= new AccountDto("1", "Finances", 15, null, null, null, 0);

        List<Account> accountList= new ArrayList<>();
        accountList.add(account1);

        Mockito.when(accountRepo.findAll())
                .thenReturn(accountList);
       Mockito.when(accountMapper.mapToDto(Mockito.any(Account.class)))
               .thenReturn(accountDto1);

        List<AccountDto> ActualAccountDtoList= accountService.accountsList();

        Assertions.assertThat(ActualAccountDtoList.get(0).name()).isEqualTo(accountList.get(0).getName());

    }
}
