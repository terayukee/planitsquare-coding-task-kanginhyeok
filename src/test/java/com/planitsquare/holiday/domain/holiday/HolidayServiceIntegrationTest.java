package com.planitsquare.holiday.domain.holiday;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import com.planitsquare.holiday.domain.holiday.service.HolidayService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Tag("integration") // ⏱ 느리거나 외부 연동이 필요한 테스트 구분
class HolidayServiceIntegrationTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void 최근_5개년_전체_국가의_공휴일을_일괄_적재한다() {
        // when
        holidayService.bulkSyncAll();

        // then
        List<Holiday> holidays = holidayRepository.findAll();
        assertFalse(holidays.isEmpty(), "전체 국가의 공휴일이 저장되어야 함");
        System.out.println("총 적재된 공휴일 수: " + holidays.size());
    }
}
