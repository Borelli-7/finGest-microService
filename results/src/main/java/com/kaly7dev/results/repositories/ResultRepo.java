package com.kaly7dev.results.repositories;

import com.kaly7dev.results.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepo extends JpaRepository<Result, Long> {

}
