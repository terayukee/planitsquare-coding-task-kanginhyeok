package com.planitsquare.holiday.domain.holiday;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import com.planitsquare.holiday.domain.holiday.service.HolidayService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void 특정_연도_국가의_공휴일을_저장한다() {
        // given
        String countryCode = "KR";
        int year = 2025;

        // when
        holidayService.sync(year, countryCode);

        // then
        List<Holiday> holidays = holidayRepository.findByYearAndCountryCode(year, countryCode);
        assertFalse(holidays.isEmpty());
        assertEquals(year, holidays.get(0).getDate().getYear());
    }

    @Test
    void 최근_5개년_전체_국가의_공휴일을_일괄_적재한다() {
        // when
        holidayService.bulkSyncAll();

        // then
        List<Holiday> holidays = holidayRepository.findAll();
        assertFalse(holidays.isEmpty(), "전체 국가의 공휴일이 저장되어야 함");
    }

}
