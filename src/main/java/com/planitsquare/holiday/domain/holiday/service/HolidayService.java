package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import com.planitsquare.holiday.domain.country.dto.CountryResponse;
import com.planitsquare.holiday.domain.holiday.client.PublicHolidayClient;
import com.planitsquare.holiday.domain.holiday.dto.HolidaySearchRequest;
import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayService {

    private static final int END_YEAR = LocalDate.now().getYear();
    private static final int START_YEAR = END_YEAR - 5;

    private final CountryApiClient countryApiClient;
    private final PublicHolidayClient publicHolidayClient;
    private final HolidayRepository holidayRepository;

    /**
     * 비동기 처리
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> syncAsync(int year, String countryCode) {
        try {
            List<PublicHolidayResponse> response = publicHolidayClient.getHolidays(year, countryCode);
            List<Holiday> holidays = response.stream()
                    .map(r -> Holiday.builder()
                            .date(r.getDate())
                            .name(r.getName())
                            .localName(r.getLocalName())
                            .countryCode(r.getCountryCode())
                            .global(r.isGlobal())
                            .fixed(r.isFixed())
                            .types(r.getTypes())
                            .build()
                    )
                    .toList();
            holidayRepository.saveAll(holidays);
            log.info("✅ {}년 {} 공휴일 저장 완료", year, countryCode);
        } catch (Exception e) {
            log.warn("❌ {}년 {} 공휴일 저장 실패: {}", year, countryCode, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 전체 연도 × 국가 조합 병렬 처리
     */
    public void bulkSyncAll() {
        List<CountryResponse> countries = countryApiClient.getAvailableCountries();

        List<CompletableFuture<Void>> futures = countries.stream()
                .flatMap(country ->
                        IntStream.rangeClosed(START_YEAR, END_YEAR)
                                .mapToObj(year -> syncAsync(year, country.getCountryCode()))
                )
                .collect(Collectors.toList());

        // 모든 비동기 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }


    public void sync(int year, String countryCode) {
        List<PublicHolidayResponse> response = publicHolidayClient.getHolidays(year, countryCode);
        List<Holiday> holidays = response.stream()
                .map(r -> Holiday.builder()
                        .date(r.getDate())
                        .name(r.getName())
                        .localName(r.getLocalName())
                        .countryCode(r.getCountryCode())
                        .global(r.isGlobal())
                        .fixed(r.isFixed())
                        .types(r.getTypes())
                        .build()
                )
                .toList();
        holidayRepository.saveAll(holidays);
    }

    public void syncByYear(int year) {
        List<CountryResponse> countries = countryApiClient.getAvailableCountries();
        for (CountryResponse country : countries) {
            sync(year, country.getCountryCode());
        }
    }

    public void syncByCountry(String countryCode) {
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            sync(year, countryCode);
        }
    }

    public Page<Holiday> search(HolidaySearchRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());
        return holidayRepository.searchByYearAndCountryCode(
                request.getYear(),
                request.getCountryCode(),
                pageRequest
        );
    }
}
