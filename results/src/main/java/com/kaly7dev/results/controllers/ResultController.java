package com.kaly7dev.results.controllers;

import com.kaly7dev.results.dtos.ResultDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ResultController {
    ResponseEntity<ResultDto> createResult(ResultDto resultDto);
    ResponseEntity<ResultDto> updateResult(ResultDto resultDto);
    ResponseEntity<String> deleteResult(Long resultID);
    ResponseEntity<ResultDto> getResultById(Long resultID);
    ResponseEntity<Map<String, Object>> listResults(boolean inflow,
                                                    boolean fixedOutflow,
                                                    String desc,
                                                    int weekNumber,
                                                    int page,
                                                    int size);
}
