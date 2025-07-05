package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import com.planitsquare.holiday.domain.country.dto.CountryResponse;
import com.planitsquare.holiday.domain.holiday.client.PublicHolidayClient;
import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidaySyncService {

    private static final int END_YEAR = LocalDate.now().getYear();
    private static final int START_YEAR = END_YEAR - 5;

    private final CountryApiClient countryApiClient;
    private final PublicHolidayClient publicHolidayClient;
    private final HolidayRepository holidayRepository;

    public void sync(int year, String countryCode) {
        List<Holiday> holidays = fetchAndConvertHolidays(year, countryCode);
        holidayRepository.saveAll(holidays);
    }

    public void syncByYear(int year) {
        List<CountryResponse> countries = countryApiClient.getAvailableCountries();
        for (CountryResponse country : countries) {
            sync(year, country.getCountryCode());
        }
    }

    public void syncByCountry(String countryCode) {
        for (int year : getTargetYears()) {
            sync(year, countryCode);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> syncAsync(int year, String countryCode) {
        try {
            sync(year, countryCode);
            log.info("✅ {}년 {} 공휴일 저장 완료", year, countryCode);
        } catch (Exception e) {
            log.warn("❌ {}년 {} 공휴일 저장 실패: {}", year, countryCode, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public void bulkSyncAll() {
        List<CountryResponse> countries = countryApiClient.getAvailableCountries();

        List<CompletableFuture<Void>> futures = countries.stream()
                .flatMap(country ->
                        getTargetYears().stream()
                                .map(year -> syncAsync(year, country.getCountryCode()))
                )
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Transactional
    public void refresh(int year, String countryCode) {
        holidayRepository.deleteByYearAndCountryCode(year, countryCode);
        sync(year, countryCode);
    }

    // 🔽 내부 유틸 메서드들

    private List<Integer> getTargetYears() {
        return IntStream.rangeClosed(START_YEAR, END_YEAR)
                .boxed()
                .toList();
    }

    private List<Holiday> fetchAndConvertHolidays(int year, String countryCode) {
        List<PublicHolidayResponse> response = publicHolidayClient.getHolidays(year, countryCode);
        return response.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private Holiday toEntity(PublicHolidayResponse r) {
        return Holiday.builder()
                .date(r.getDate())
                .name(r.getName())
                .localName(r.getLocalName())
                .countryCode(r.getCountryCode())
                .global(r.isGlobal())
                .fixed(r.isFixed())
                .types(r.getTypes())
                .build();
    }
}
