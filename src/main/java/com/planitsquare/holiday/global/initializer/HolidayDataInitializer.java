package com.planitsquare.holiday.global.initializer;

import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 실행 시 최근 5년간 전체 국가의 공휴일을 초기 적재합니다.
 * 단, 기존 데이터가 존재할 경우 생략합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class HolidayDataInitializer implements ApplicationRunner {

    private final HolidayRepository holidayRepository;
    private final HolidaySyncService holidaySyncService;

    @Override
    public void run(ApplicationArguments args) {
        if (holidayRepository.count() > 0) {
            log.info("✅ 기존 공휴일 데이터가 존재하므로 초기 적재를 생략합니다.");
            return;
        }

        log.info("🚀 최초 실행: 최근 5년간 전체 국가 공휴일 데이터를 적재합니다.");
        holidaySyncService.bulkSyncAll();
        log.info("🎉 초기 데이터 적재 완료");
    }
}
