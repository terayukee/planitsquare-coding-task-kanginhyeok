package com.planitsquare.holiday.domain.holiday;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import com.planitsquare.holiday.domain.holiday.client.PublicHolidayClient;
import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class HolidaySyncTest {

    @Autowired
    private HolidaySyncService holidaySyncService;

    @Autowired
    private HolidayRepository holidayRepository;

    @MockBean
    private PublicHolidayClient publicHolidayClient;

    @MockBean
    private CountryApiClient countryApiClient;

    @Test
    void 특정_연도_국가의_공휴일을_저장한다() {
        // given
        String countryCode = "KR";
        int year = 2025;

        PublicHolidayResponse mockResponse = new PublicHolidayResponse();
        mockResponse.setDate(LocalDate.of(2025, 12, 25));
        mockResponse.setName("Christmas Day");
        mockResponse.setLocalName("크리스마스");
        mockResponse.setCountryCode("KR");
        mockResponse.setGlobal(true);

        when(publicHolidayClient.getHolidays(year, countryCode))
                .thenReturn(List.of(mockResponse));

        // when
        holidaySyncService.sync(year, countryCode);

        // then
        List<Holiday> holidays = holidayRepository.findByYearAndCountryCode(year, countryCode);
        assertFalse(holidays.isEmpty());
        assertEquals("크리스마스", holidays.get(0).getLocalName());
    }
}
