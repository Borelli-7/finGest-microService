package com.kaly7dev.results.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.entities.Result;
import com.kaly7dev.results.mappers.ResultMapper;
import com.kaly7dev.results.repositories.ResultRepo;
import com.kaly7dev.results.services.ResultService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ResultControllerRestAPiImpl.class)
class ResultControllerRestAPiImplTest {
    @MockBean
    private ResultService resultService;

    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("Should list all results when making Get resquest to endpoint -" +
            " /api/result/getlist/inflow={inflow}andfixedOutflow={fixedOutflow}\" ")
    void testListResult() throws Exception{
        ResultDto resultDto1= new ResultDto(
                1L, "test retrieve result1", BigDecimal.valueOf(1000), null,
                null, false, 34, true);
        ResultDto resultDto2= new ResultDto(
                2L, "test retrieve result2", BigDecimal.valueOf(1000), null,
                null, false, 20, true);

        Mockito.when(resultService.listResults(false, true))
                .thenReturn(asList(resultDto1, resultDto2));

        mockMvc.perform(get("/api/result/getlist/inflow=falseandfixedOutflow=true"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].resultID", Matchers.is(1)))
                .andExpect(jsonPath("$[0].description", Matchers.is("test retrieve result1")))
                .andExpect(jsonPath("$[0].weekNumber", Matchers.is(34)))
                .andExpect(jsonPath("$[1].resultID", Matchers.is(2)))
                .andExpect(jsonPath("$[1].description", Matchers.is("test retrieve result2")))
                .andExpect(jsonPath("$[1].weekNumber", Matchers.is(20)));

    }
    @Test
    @DisplayName("Should Return resultDto created !" )
    void testCreateResult() throws Exception {
/*        ResultDto expectedResultDto= new ResultDto(
                1L, "test retrieve result", BigDecimal.valueOf(1000), Instant.now(),
                null, false, 20, true);
        ResultDto resultDto= new ResultDto(
                0L, "test retrieve result", BigDecimal.valueOf(1000), null,
                null, false, 20, true);

*//*        Mockito.when(resultService.createResult(resultDto))
                .thenReturn(expectedResultDto);*//*

        mockMvc.perform(post("/api/result/create"))
                .andExpect(content()
                        .json(asJsonString(resultDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));*/

    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
