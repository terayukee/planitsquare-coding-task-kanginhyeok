package com.planitsquare.holiday.domain.holiday;

import com.planitsquare.holiday.domain.holiday.service.HolidaySyncScheduler;
import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Year;

import static org.mockito.Mockito.*;

@SpringBootTest
class HolidaySyncSchedulerTest {

    @SpyBean
    private HolidaySyncService holidaySyncService;

    @Autowired
    private HolidaySyncScheduler scheduler;

    @Test
    void 매년_1월2일_자동_배치_동기화() {
        // given
        int currentYear = Year.now().getValue();
        int previousYear = currentYear - 1;

        // when
        scheduler.syncPreviousAndCurrentYear();

        // then
        verify(holidaySyncService, times(1)).syncByYear(previousYear);
        verify(holidaySyncService, times(1)).syncByYear(currentYear);
    }
}
