package com.kaly7dev.accounts;

import com.kaly7dev.accounts.models.User;
import com.kaly7dev.accounts.repositories.AccountRepo;
import com.kaly7dev.accounts.repositories.UserRepo;
import com.kaly7dev.accounts.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RefreshScope
@AllArgsConstructor
@Slf4j
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AccountService accountService,
										AccountRepo accountRepo,
										UserRepo userRepo){
		return args -> {
			if(!userRepo.findAll().isEmpty()){
				List<String> names = List.of("Borel", "Sorel", "Orline");
				List<User> userList= new ArrayList<>();
				User user= new User();
				var i= 0;
				for (String name : names) {
					user.setUserID(String.valueOf(i));
					user.setFirstName(name);
					i++;
					userList.add(user);
				}
				userRepo.saveAll(userList);
				log.info("Users list Successfully Initialized !");
			} else {
				log.info("Users list Successfully Restored !");
			}

			accountService.initializeAccountsUser( );
		};
	}

}

