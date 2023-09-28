package com.kaly7dev.accounts.repositories;

import com.kaly7dev.accounts.entities.Account;
import com.kaly7dev.accounts.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends MongoRepository<Account, String> {
    Optional<Account> findByName(String name);
    List<Account> findAllByOwner(User user);
    Optional<Account> findByNameAndOwner_UserID(String name, String userID);
}
