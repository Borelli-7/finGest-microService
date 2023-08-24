package com.kaly7dev.results.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.services.ResultService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ResultControllerRestAPiImpl.class)
class ResultControllerRestAPiImplTest {
    @MockBean
    private ResultService resultService;
    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper mapper = new ObjectMapper();

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
        ResultDto resultDto= new ResultDto(
                1L, "test retrieve result1", BigDecimal.valueOf(1000), null,
                null, false, 34, true);

        Mockito.when(resultService.createResult(ArgumentMatchers.any(ResultDto.class)))
                .thenReturn(resultDto);
        String json= mapper.writeValueAsString(resultDto);

        mockMvc.perform(post("/api/result/create").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultID", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.description", Matchers.equalTo("test retrieve result1")));
    }
    @Test
    @DisplayName("should return resultDto updated ! ")
    void testUpdateResult() throws Exception{
        ResultDto resultDto= new ResultDto(
                1L, "test retrieve result1", BigDecimal.valueOf(12000), null,
                null, false, 34, true);

        Mockito.when(resultService.updateResult(ArgumentMatchers.any(ResultDto.class)))
                .thenReturn(resultDto);
        String json= mapper.writeValueAsString(resultDto);

        mockMvc.perform(put("/api/result/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultID", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.amount", Matchers.equalTo(12000)));
    }

    @Test
    void testDeleteResult() throws Exception{
        Mockito.when(resultService.deleteResult(ArgumentMatchers.anyLong()))
                .thenReturn("Result Successfully Deleted !");

        //mockMvc.perform(delete("/api/result/delete").param("resultID", "1")) // if I used @RequestParam on resultcontroller
        MvcResult requestResult= mockMvc.perform(delete("/api/result/delete/1"))
                .andExpect(status().isOk())
                .andReturn();

                String resultString= requestResult.getResponse()
                        .getContentAsString();

        Assertions.assertThat(resultString)
                .isEqualTo("Result Successfully Deleted !");
    }
}
