package com.kaly7dev.accounts.repositories;

import com.kaly7dev.accounts.entities.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends MongoRepository<Account, String> {
    Optional<Account> findByName(String name);
}
