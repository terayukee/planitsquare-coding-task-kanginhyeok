package com.planitsquare.holiday.domain.holiday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HolidaySearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ 연도와 국가코드 기준 공휴일 검색 성공")
    void searchHolidays_basic_success() throws Exception {
        mockMvc.perform(get("/api/holidays")
                        .param("year", "2025")
                        .param("countryCode", "KR")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()", lessThanOrEqualTo(5)))
                .andExpect(jsonPath("$.data.totalElements", greaterThan(0)))
                .andExpect(jsonPath("$.data.content[0].countryCode").value("KR"));
    }

    @Test
    @DisplayName("📆 날짜 범위 필터링 테스트")
    void searchHolidays_withDateFilter() throws Exception {
        mockMvc.perform(get("/api/holidays")
                        .param("year", "2025")
                        .param("countryCode", "KR")
                        .param("from", "2025-01-01")
                        .param("to", "2025-05-01")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[*].date", everyItem(startsWith("2025-0"))));
    }

    @Test
    @DisplayName("⚠️ 필수 파라미터 누락 시 400 에러")
    void searchHolidays_missingRequired() throws Exception {
        mockMvc.perform(get("/api/holidays")
                        .param("countryCode", "KR"))  // year 빠짐
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("🔎 공휴일 유형 필터링 테스트")
    void searchHolidays_withTypeFilter() throws Exception {
        mockMvc.perform(get("/api/holidays")
                        .param("year", "2025")
                        .param("countryCode", "KR")
                        .param("types", "Public")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[*].types[*]", hasItem("Public")));
    }
}
