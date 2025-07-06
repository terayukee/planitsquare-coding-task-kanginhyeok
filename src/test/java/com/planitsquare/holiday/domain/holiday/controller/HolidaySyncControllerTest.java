package com.planitsquare.holiday.domain.holiday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HolidaySyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HolidaySyncService holidaySyncService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 공휴일_데이터를_지정_연도_및_국가에_대해_동기화한다() throws Exception {
        mockMvc.perform(post("/api/holidays/sync")
                        .param("year", "2025")
                        .param("countryCode", "KR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 특정_연도_전체_국가에_대해_동기화한다() throws Exception {
        mockMvc.perform(post("/api/holidays/sync/year")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 특정_국가_최근_5년치_데이터를_동기화한다() throws Exception {
        mockMvc.perform(post("/api/holidays/sync/country")
                        .param("countryCode", "KR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 모든_국가에_대한_전체_동기화를_비동기로_수행한다() throws Exception {
        mockMvc.perform(post("/api/holidays/sync/bulk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 기존_데이터를_삭제하고_새로_동기화한다() throws Exception {
        mockMvc.perform(put("/api/holidays/refresh")
                        .param("year", "2025")
                        .param("countryCode", "KR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 특정_연도_및_국가_공휴일_데이터를_삭제한다() throws Exception {
        mockMvc.perform(delete("/api/holidays")
                        .param("year", "2025")
                        .param("countryCode", "KR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
