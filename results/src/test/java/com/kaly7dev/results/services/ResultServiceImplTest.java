package com.kaly7dev.results.services;

import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.entities.Result;
import com.kaly7dev.results.mappers.ResultMapper;
import com.kaly7dev.results.repositories.ResultRepo;
import com.kaly7dev.results.exceptions.ResultNotFoundException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {
    @Mock
    private  ResultMapper resultMapper;
    @Mock
    private  ResultRepo resultRepo;

    @Captor
    private ArgumentCaptor<Result> resultArgumentCaptor;

    private ResultService resultService;

    @BeforeEach
    void setUp() {
        resultService= new ResultServiceImpl(resultMapper, resultRepo) ;
    }

    @Test
    @DisplayName("Should Create Results ")
    void testCreateResult() {
        Result result= new Result(
                1L, "test create result", BigDecimal.valueOf(1000), Instant.now(),
                null, true, 34, true);
        ResultDto resultDto= new ResultDto(
                0L, "test create result", BigDecimal.valueOf(1000), null,
                null, true, 0, true);

        Mockito.when(resultMapper.mapToResult(resultDto))
                .thenReturn(result);

        resultService.createResult(resultDto);
        Mockito.verify(resultRepo, Mockito.times(1))
                .save(resultArgumentCaptor.capture());

        Assertions.assertThat(resultArgumentCaptor.getValue().getResultID()).isEqualTo(1L);
        Assertions.assertThat(resultArgumentCaptor.getValue().getWeekNumber()).isEqualTo(34);
    }

    @Test
    @DisplayName("Should Retrieve Result by ID")
    void getResultById(){
        Result result= new Result(
                123L, "test retrieve result", BigDecimal.valueOf(1000), Instant.now(),
                Instant.now(), true, 34, true);
        ResultDto expectedResultDto= new ResultDto(
                123L, "test retrieve result", BigDecimal.valueOf(1000), Instant.now(),
                Instant.now(), true, 34, true);

        Mockito.when(resultRepo.findById(123L))
                .thenReturn(Optional.of(result));
        Mockito.when(resultMapper.mapToDto(Mockito.any(Result.class)))
                .thenReturn(expectedResultDto);

        ResultDto ActualResultDto= resultService.getResultById(123L);

        Assertions.assertThat(ActualResultDto.resultID())
                .isEqualTo(expectedResultDto.resultID());
        Assertions.assertThat(ActualResultDto.weekNumber())
                .isEqualTo(expectedResultDto.weekNumber());
    }

    @Test
    @DisplayName("Should Throw Exception when Result ID not exist in DB")
    void shouldFailWhenResultIDNotExist() {

        Assertions.assertThatThrownBy(
                ()-> {
                   resultService.getResultById(213L);
                }
        ).isInstanceOf(ResultNotFoundException.class)
                .hasMessage("Result ID not found In DataBase !");
    }

    @Test
    @DisplayName("Should delete result in Database")
    void testDeleteResult() {
        Result result= new Result(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, true, 34, true);
        Optional<Result> optionalResult= Optional.of(result);

        Mockito.when(resultRepo.findById(123L))
                .thenReturn(optionalResult);

        resultService.deleteResult(123L);
        Mockito.verify(resultRepo, Mockito.times(0))
                .delete(resultArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should list only the Fixed Outflow results ! ")
    void testListFixedOutFlowResults() {
        Result result1= new Result(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, false, 34, true);
        ResultDto resultDto= new ResultDto(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, false, 34, true);

        List<Result> resultList= new ArrayList<>();
        resultList.add(result1);
        List<ResultDto> expectedResultDtoList= new ArrayList<>();
        expectedResultDtoList.add(resultDto);

        Mockito.when(resultRepo.findAll())
                        .thenReturn(resultList);
        Mockito.when(resultMapper.mapToDto(Mockito.any(Result.class)))
                        .thenReturn(resultDto);

        List<ResultDto> actualResultDtoList=
                resultService.listResults(false,true);

        Assertions.assertThat(actualResultDtoList.contains(resultDto))
                .isEqualTo(expectedResultDtoList.contains(resultDto));
    }

    @Test
    @DisplayName("Should list only the inflow results ! ")
    void testListInflowResults() {
        Result result1= new Result(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, true, 34, false);
        ResultDto resultDto= new ResultDto(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, true, 34, false);

        List<Result> resultList= new ArrayList<>();
        resultList.add(result1);
        List<ResultDto> expectedResultDtoList= new ArrayList<>();
        expectedResultDtoList.add(resultDto);

        Mockito.when(resultRepo.findAll())
                .thenReturn(resultList);
        Mockito.when(resultMapper.mapToDto(Mockito.any(Result.class)))
                .thenReturn(resultDto);

        List<ResultDto> actualInflowResultDtoList=
                resultService.listResults(true,false);

        Assertions.assertThat(actualInflowResultDtoList.contains(resultDto))
                .isEqualTo(expectedResultDtoList.contains(resultDto));
    }

    @Test
    @DisplayName("Should list only the Variables Outflows results ! ")
    void testListvariableOutflowResults() {
        Result result1= new Result(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, false, 34, false);
        ResultDto resultDto= new ResultDto(
                123L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, false, 34, false);

        List<Result> resultList= new ArrayList<>();
        resultList.add(result1);
        List<ResultDto> expectedResultDtoList= new ArrayList<>();
        expectedResultDtoList.add(resultDto);

        Mockito.when(resultRepo.findAll())
                .thenReturn(resultList);
        Mockito.when(resultMapper.mapToDto(Mockito.any(Result.class)))
                .thenReturn(resultDto);

        List<ResultDto> actualInflowResultDtoList=
                resultService.listResults(false,false);

        Assertions.assertThat(actualInflowResultDtoList.contains(resultDto))
                .isEqualTo(expectedResultDtoList.contains(resultDto));
    }
}