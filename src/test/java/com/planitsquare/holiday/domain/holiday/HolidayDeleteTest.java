// HolidayDeleteTest.java
package com.planitsquare.holiday.domain.holiday;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HolidayDeleteTest {

    @Autowired
    private HolidaySyncService holidaySyncService;

    @Autowired
    private HolidayRepository holidayRepository;

    @BeforeEach
    void setUp() {
        holidayRepository.deleteAll();

        Holiday holiday = Holiday.builder()
                .date(LocalDate.of(2024, 8, 15))
                .name("광복절")
                .localName("광복절")
                .countryCode("KR")
                .global(true)
                .fixed(true)
                .types(List.of("Public"))
                .build();

        holidayRepository.save(holiday);
    }

    @Test
    void 연도_국가_기반_공휴일_삭제() {
        // when
        holidaySyncService.delete(2024, "KR");

        // then
        List<Holiday> remains = holidayRepository.findByYearAndCountryCode(2024, "KR");
        assertTrue(remains.isEmpty());
    }
}
