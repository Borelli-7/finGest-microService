package com.kaly7dev.accounts.repositories;

import com.kaly7dev.accounts.models.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepo extends MongoRepository<Address, Long> {
}
