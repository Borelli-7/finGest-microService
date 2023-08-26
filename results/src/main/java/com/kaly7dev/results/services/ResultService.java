package com.kaly7dev.results.services;

import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.entities.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ResultService {

    ResultDto createResult(ResultDto resultDto);

    ResultDto updateResult(ResultDto resultDto);

    String deleteResult(Long resultID);

    ResultDto getResultById(Long resultID);

    Map< String, Object> listResults(boolean isInflow,
                                      boolean isFixedOutflow,
                                      String desc,
                                      int weekNumber,
                                      int page,
                                      int size);

    public Page<Result> selectSearchFunction(String desc, int weekNumber, Pageable paging);
}
