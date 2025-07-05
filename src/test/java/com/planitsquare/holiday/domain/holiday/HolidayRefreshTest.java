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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HolidayRefreshTest {

    @Autowired
    private HolidaySyncService holidaySyncService;

    @Autowired
    private HolidayRepository holidayRepository;

    @BeforeEach
    void setUp() {
        holidayRepository.deleteAll();
        holidayRepository.save(
                Holiday.builder()
                        .date(LocalDate.of(2025, 5, 1))
                        .name("Dummy")
                        .localName("가짜")
                        .countryCode("KR")
                        .global(false)
                        .fixed(true)
                        .types(List.of("Public"))
                        .build()
        );
    }

    @Test
    void 연도_국가_기반_공휴일_재동기화() {
        // given: 더미 데이터가 저장되어 있음
        assertEquals(1, holidayRepository.count());

        // when: 재동기화 수행
        holidaySyncService.refresh(2025, "KR");

        // then: 실제 API 데이터를 기준으로 다시 저장되며, 기존 더미 데이터는 제거됨
        List<Holiday> results = holidayRepository.findByYearAndCountryCode(2025, "KR");

        assertTrue(results.size() > 0);
        assertTrue(results.stream().noneMatch(h -> h.getName().equals("Dummy")));
    }
}
