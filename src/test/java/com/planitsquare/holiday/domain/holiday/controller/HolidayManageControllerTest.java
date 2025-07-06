package com.planitsquare.holiday.domain.holiday.controller;

import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidayManageController.class)
class HolidayManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidaySyncService holidaySyncService;

    @Test
    @DisplayName("🗑️ 공휴일 삭제 성공")
    void deleteHoliday_shouldReturnSuccess() throws Exception {
        int year = 2023;
        String countryCode = "KR";

        doNothing().when(holidaySyncService).delete(year, countryCode);

        mockMvc.perform(delete("/api/holidays")
                        .param("year", String.valueOf(year))
                        .param("countryCode", countryCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("🔄 공휴일 갱신 성공")
    void refreshHoliday_shouldReturnSuccess() throws Exception {
        int year = 2024;
        String countryCode = "US";

        doNothing().when(holidaySyncService).refresh(year, countryCode);

        mockMvc.perform(put("/api/holidays/refresh")
                        .param("year", String.valueOf(year))
                        .param("countryCode", countryCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200));
    }
}
