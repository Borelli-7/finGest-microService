package com.kaly7dev.results.services;

import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.entities.Result;
import com.kaly7dev.results.exceptions.ResultNotFoundException;
import com.kaly7dev.results.mappers.ResultMapper;
import com.kaly7dev.results.repositories.ResultRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultMapper resultMapper;
    private final ResultRepo resultRepo;
    private static final String ID_NOT_FOUND = "Result ID not found In DataBase !";

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public String deleteResult(Long resultID) {
        Result findedResult= findById(resultID);

        resultRepo.deleteById(findedResult.getResultID());

        log.info("Result Successfully Deleted !");
        return "Result Successfully Deleted !";
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDto getResultById(Long resultID) {
        Result findedResult= findById(resultID);

        ResultDto resultDtoSaved= resultMapper.mapToDto(findedResult);

        log.info("Result Successfully Found ! ");
        return resultDtoSaved;
    }


    @Override
    public Map< String, Object> listResults(boolean isInflow,
                                             boolean isFixedOutflow,
                                             String desc,
                                             int weekNumber,
                                             int page,
                                             int size){

        Pageable paging= PageRequest.of(page, size);

        Page<Result> pageResultList= selectSearchFunction(desc, weekNumber, paging);

        List<Result> resultList= pageResultList.getContent();

            List<ResultDto> resultDtoList= getListResults(isInflow, isFixedOutflow, resultList);

            Map< String, Object> response= new HashMap<>();
            response.put("results List", resultDtoList);
            response.put("current Page", pageResultList.getNumber());
            response.put("total Items", pageResultList.getTotalElements());
            response.put("total Pages", pageResultList.getTotalPages());

            return response;
    }

    private Page<Result> selectSearchFunction(String desc, int weekNumber, Pageable paging) {
        Page<Result> pageResultList;
        if ((desc != null) && (weekNumber != 0) ){
            pageResultList= resultRepo.findByDescriptionAndWeekNumber(desc, weekNumber, paging);

        }else if((desc == null) && (weekNumber != 0)){
            pageResultList= resultRepo.findByWeekNumber(weekNumber, paging);

        } else if (desc != null) {
            pageResultList= resultRepo.findByDescription(desc, paging);
        }else {
            pageResultList= resultRepo.findAll(paging);
        }
        return pageResultList;
    }

    private List<ResultDto> getDtosVariableOutFlowList(List<Result> resultList) {
        List<ResultDto> variableOuFlowsList= resultList.parallelStream()
                            .filter(result -> (!result.isInFlow() && !result.isFixedOutflow()))
                            .map(resultMapper::mapToDto)
                            .toList();

        log.info("Results Variables Outflows successfully listed ! ");
        return variableOuFlowsList;
    }

    private List<ResultDto> getDtosFixedOutFlowList(List<Result> resultList) {
        List<ResultDto> fixedOuFlowsList= resultList.parallelStream()
                            .filter(result -> (!result.isInFlow() && result.isFixedOutflow()))
                            .map(resultMapper::mapToDto)
                            .toList();

        log.info("Results Fixed Outflows successfully listed ! ");
        return fixedOuFlowsList;
    }

    private List<ResultDto> getDtosInflowList(List<Result> resultList) {
        List<ResultDto> inflowList= resultList.parallelStream()
                            .filter(Result::isInFlow)
                            .map(resultMapper::mapToDto)
                            .toList();
        log.info("Results Inflows successfully listed ! ");
        return inflowList;
    }

    private List<ResultDto> getListResults(
            boolean isInflow,
            boolean isFixedOutflow,
            List<Result> resultList) {

        if (isInflow){
            return getDtosInflowList(resultList);

        } else if (isFixedOutflow) {
            return getDtosFixedOutFlowList(resultList);
        }else {
            return getDtosVariableOutFlowList(resultList);
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
