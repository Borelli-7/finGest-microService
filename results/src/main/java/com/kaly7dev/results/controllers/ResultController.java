package com.kaly7dev.results.controllers;

import com.kaly7dev.results.dtos.ResultDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ResultController {
    ResponseEntity<ResultDto> createResult(ResultDto resultDto);
    ResultDto updateResult(ResultDto resultDto);
    ResponseEntity<Void> deleteResult(Long resultID);
    ResponseEntity<List<ResultDto>> listResults(boolean isInflow, boolean isFixedOutflow);

    ResponseEntity<ResultDto> getResultById(Long resultID);
}
