package com.kaly7dev.accounts;

import com.kaly7dev.accounts.repositories.AccountRepo;
import com.kaly7dev.accounts.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RefreshScope
@AllArgsConstructor
public class AccountsApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(AccountService accountService, AccountRepo accountRepo){
		return args -> accountService.initializeAccounts(accountRepo);
	}

}

