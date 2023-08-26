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
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
            " /api/result/getlist/{inflow}and{fixedOutflow}")
    void testListResult() throws Exception{
        ResultDto resultDto1= new ResultDto(
                1L, "result", BigDecimal.valueOf(1000), null,
                null, false, 34, true);
        ResultDto resultDto2= new ResultDto(
                2L, "result", BigDecimal.valueOf(1000), null,
                null, false, 34, true);

        Map<String, Object> response= new HashMap<>();
        response.put("results List", asList(resultDto1, resultDto2));
        response.put("current Page", 0);
        response.put("total Items", 2);
        response.put("total Pages", 1);

        Mockito.when(resultService.listResults(false, true, "test retrieve result1",
                        34, 0, 3))
                .thenReturn(response);

        mockMvc.perform(get("/api/result/getlist/falseandtrue")
                        .param("desc", "test retrieve result1")
                        .param("weekNumber", "34")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.['results List'].size()", Matchers.is(2)))
                .andExpect(jsonPath("$['results List'].[0].resultID", Matchers.is(1)))
                .andExpect(jsonPath("$['results List'].[1].resultID", Matchers.is(2)));


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
