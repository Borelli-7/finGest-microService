package com.kaly7dev.results.services;

import com.kaly7dev.results.dtos.ResultDto;

import java.util.List;

public interface ResultService {

    ResultDto createResult(ResultDto resultDto);

    ResultDto updateResult(ResultDto resultDto);

    void deleteResult(Long resultID);

    ResultDto getResultById(Long resultID);

    List<ResultDto> listResults(boolean isInflow, boolean isFixedOutflow);
}
