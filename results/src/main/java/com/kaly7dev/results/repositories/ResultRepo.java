package com.kaly7dev.results.repositories;

import com.kaly7dev.results.entities.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepo extends JpaRepository<Result, Long> {
    Page<Result> findByDescription(String desc, Pageable pageable);
    Page<Result> findByWeekNumber(int weeknumber, Pageable pageable);
    Page<Result> findByDescriptionAndWeekNumber(String desc, int weekN, Pageable pageable);
}
