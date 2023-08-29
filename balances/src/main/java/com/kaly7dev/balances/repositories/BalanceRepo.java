package com.kaly7dev.balances.repositories;

import com.kaly7dev.balances.entities.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepo extends JpaRepository<Balance, Long> {
    Page<Balance> findByDescription(String desc, Pageable pageable);
    Page<Balance> findByPrice(double price, Pageable pageable);
    Page<Balance> findByDescriptionAndPrice(String desc, double price, Pageable pageable);
}
