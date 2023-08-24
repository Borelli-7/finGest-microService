package com.kaly7dev.results.controllers;

import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.services.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/result")
@AllArgsConstructor
public class ResultControllerRestAPiImpl implements ResultController {

    private final ResultService resultService;
    @PostMapping("/create")
    public ResponseEntity<ResultDto> createResult(@RequestBody ResultDto resultDto) {
        return status(CREATED)
                .body(resultService.createResult(resultDto));
    }

    @Override
    @PutMapping("/update")
    public ResultDto updateResult(@RequestBody ResultDto resultDto) {
        return resultService.updateResult(resultDto);
    }

    @Override
    @DeleteMapping("/delete/{resultID}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long resultID) {
        resultService.deleteResult(resultID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/getlist/inflow={inflow}andfixedOutflow={fixedOutflow}")
    public ResponseEntity<List<ResultDto>> listResults(
            @PathVariable boolean inflow,
            @PathVariable boolean fixedOutflow
    ) {
        return status(OK).body(resultService.listResults(inflow, fixedOutflow));
    }

    @Override
    @GetMapping("/findbyid/{resultID}")
    public ResponseEntity<ResultDto> getResultById(@PathVariable Long resultID) {
        return status(HttpStatus.OK).body(resultService.getResultById(resultID));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
