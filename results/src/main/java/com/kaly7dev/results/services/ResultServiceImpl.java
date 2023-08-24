package com.kaly7dev.results.services;

import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.entities.Result;
import com.kaly7dev.results.exceptions.ResultNotFoundException;
import com.kaly7dev.results.mappers.ResultMapper;
import com.kaly7dev.results.repositories.ResultRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultMapper resultMapper;
    private final ResultRepo resultRepo;
    private static final String ID_NOT_FOUND = "Result ID not found In DataBase !";

    @Override
    public ResultDto createResult( ResultDto resultDto) {
        Result resultToSave= resultMapper.mapToResult(resultDto);

        resultToSave.setCreatedDate(Instant.now());
        resultToSave.setWeekNumber(getWeekNumber());

        ResultDto resultDtoCreated= resultMapper.mapToDto(resultRepo
                .save(resultToSave));

        log.info("Result Successfully Created!");
        return resultDtoCreated;
    }

    @Override
    public ResultDto updateResult(ResultDto resultDto) {
        Result findedResult= findById(resultDto.resultID());

        findedResult.setUpdatedDate(Instant.now());
        findedResult.setDescription(resultDto.description());
        findedResult.setAmount(resultDto.amount());

        ResultDto resultDtoUpdated= resultMapper.mapToDto(
                resultRepo.save(findedResult)
        );

        log.info("Result Successfully Updated !");
        return resultDtoUpdated;
    }

    @Override
    public void deleteResult(Long resultID) {
        Result findedResult= findById(resultID);

        resultRepo.deleteById(findedResult.getResultID());

        log.info("Result Successfully Deleted !");
    }

    @Override
    public ResultDto getResultById(Long resultID) {
        Result findedResult= findById(resultID);

        ResultDto resultDtoSaved= resultMapper.mapToDto(findedResult);

        log.info("Result Successfully Found ! ");
        return resultDtoSaved;
    }

    @Override
    public List<ResultDto> listResults(boolean isInflow, boolean isFixedOutflow) {
        if (isInflow){
            List<ResultDto> inflowList= resultRepo.findAll()
                                .parallelStream()
                                .filter(Result::isInFlow)
                                .map(resultMapper::mapToDto)
                                .toList();
            log.info("Results Inflows successfully listed ! ");
            return inflowList;

        } else if (isFixedOutflow) {
            List<ResultDto> fixedOuFlowsList= resultRepo.findAll()
                                .parallelStream()
                                .filter(result -> (!result.isInFlow() && result.isFixedOutflow()))
                                .map(resultMapper::mapToDto)
                                .toList();

            log.info("Results Fixed Outflows successfully listed ! ");
            return fixedOuFlowsList;
        }else {
            List<ResultDto> variableOuFlowsList= resultRepo.findAll()
                                .parallelStream()
                                .filter(result -> (!result.isInFlow() && !result.isFixedOutflow()))
                                .map(resultMapper::mapToDto)
                                .toList();

            log.info("Results Variables Outflows successfully listed ! ");
            return variableOuFlowsList;
        }

    }

    private int getWeekNumber(){
        LocalDate date = LocalDate.now();
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    private Result findById(Long id){
        return resultRepo.findById(id)
                .orElseThrow(()-> new ResultNotFoundException(ID_NOT_FOUND));
    }
}
