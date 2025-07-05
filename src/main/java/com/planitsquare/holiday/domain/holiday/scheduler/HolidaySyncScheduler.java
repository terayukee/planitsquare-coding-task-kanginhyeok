package com.planitsquare.holiday.domain.holiday.scheduler;

import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidaySyncScheduler {

    private final HolidaySyncService holidaySyncService;

    // 매년 1월 2일 01:00에 실행 (KST 기준)
    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void syncPreviousAndCurrentYear() {
        int currentYear = Year.now().getValue();
        int previousYear = currentYear - 1;

        log.info("🔁 전년도({}) 및 금년도({}) 공휴일 자동 동기화 시작", previousYear, currentYear);
        holidaySyncService.syncByYear(previousYear);
        holidaySyncService.syncByYear(currentYear);
        log.info("✅ 공휴일 자동 동기화 완료");
    }
}
