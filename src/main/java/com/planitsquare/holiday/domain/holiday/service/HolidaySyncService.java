package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import com.planitsquare.holiday.domain.country.dto.CountryResponse;
import com.planitsquare.holiday.domain.holiday.client.PublicHolidayClient;
import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.exception.HolidayErrorCode;
import com.planitsquare.holiday.domain.holiday.exception.HolidayException;
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

import static com.planitsquare.holiday.global.util.HolidayValidator.validateYear;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidaySyncService {

    private static final int END_YEAR = LocalDate.now().getYear();
    private static final int START_YEAR = END_YEAR - 5;

    private final CountryApiClient countryApiClient;
    private final PublicHolidayClient publicHolidayClient;
    private final HolidayRepository holidayRepository;

    /**
     * 특정 국가의 특정 연도 공휴일을 외부 API에서 조회하고 저장합니다.
     */
    public void sync(int year, String countryCode) {
        validateYear(year);
        try {
            List<Holiday> holidays = fetchAndConvertHolidays(year, countryCode);
            if (holidays.isEmpty()) {
                log.warn("저장할 공휴일 데이터가 없습니다: year={}, country={}", year, countryCode);
                throw new HolidayException(HolidayErrorCode.EMPTY_HOLIDAY_DATA);
            }

            holidayRepository.saveAll(holidays);
        } catch (HolidayException e) {
            throw e;
        } catch (Exception e) {
            throw new HolidayException(HolidayErrorCode.DATA_SAVE_FAILED);
        }
    }


    /**
     * 특정 연도의 모든 국가 공휴일을 동기화합니다.
     */
    public void syncByYear(int year) {
        validateYear(year);
        List<CountryResponse> countries = getAvailableCountries();

        for (CountryResponse country : countries) {
            sync(year, country.getCountryCode());
        }
    }

    /**
     * 특정 국가의 최근 5개년 공휴일을 동기화합니다.
     */
    public void syncByCountry(String countryCode) {
        for (int year : getTargetYears()) {
            sync(year, countryCode);
        }
    }

    /**
     * 비동기 방식으로 특정 연도의 특정 국가 공휴일을 동기화합니다.
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> syncAsync(int year, String countryCode) {
        validateYear(year);
        try {
            sync(year, countryCode);
            log.info("✅ {}년 {} 공휴일 저장 완료", year, countryCode);
        } catch (Exception e) {
            log.warn("❌ {}년 {} 공휴일 저장 실패: {}", year, countryCode, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 모든 국가의 최근 5개년 공휴일을 비동기로 대량 동기화합니다.
     */
    public void bulkSyncAll() {
        List<CountryResponse> countries = getAvailableCountries();

        List<CompletableFuture<Void>> futures = countries.stream()
                .flatMap(country ->
                        getTargetYears().stream()
                                .map(year -> syncAsync(year, country.getCountryCode()))
                )
                .toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("전체 공휴일 동기화 실패", e);
            throw new HolidayException(HolidayErrorCode.SYNC_FAILED);
        }

    }

    /**
     * 기존 데이터를 삭제하고 새로 동기화합니다.
     */
    @Transactional
    public void refresh(int year, String countryCode) {
        validateYear(year);
        deleteInternal(year, countryCode);
        sync(year, countryCode);
    }

    /**
     * 특정 연도/국가의 공휴일 데이터를 삭제합니다.
     */
    @Transactional
    public void delete(int year, String countryCode) {
        validateYear(year);
        deleteInternal(year, countryCode);
        log.info("🗑️ {}년 {} 공휴일 삭제 완료", year, countryCode);
    }

    // ===== 내부 유틸 메서드 =====

    /**
     * 최근 5개년을 반환합니다.
     */
    private List<Integer> getTargetYears() {
        return IntStream.rangeClosed(START_YEAR, END_YEAR)
                .boxed()
                .toList();
    }

    /**
     * 공휴일 API를 호출하고 Holiday 엔티티로 변환합니다.
     */
    private List<Holiday> fetchAndConvertHolidays(int year, String countryCode) {
        try {
            List<PublicHolidayResponse> response = publicHolidayClient.getHolidays(year, countryCode);
            if (response == null || response.isEmpty()) {
                throw new HolidayException(HolidayErrorCode.EMPTY_HOLIDAY_DATA);
            }
            return response.stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("공휴일 API 호출 실패: {}, {}", year, countryCode);
            throw new HolidayException(HolidayErrorCode.HOLIDAY_API_FAILURE);
        }
    }


    /**
     * DTO를 엔티티로 변환합니다.
     */
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

    /**
     * 특정 연도/국가의 기존 데이터를 삭제합니다.
     */
    private void deleteInternal(int year, String countryCode) {
        try {
            holidayRepository.deleteByYearAndCountryCode(year, countryCode);
        } catch (Exception e) {
            throw new HolidayException(HolidayErrorCode.SYNC_FAILED);
        }
    }

    /**
     * 외부 국가 API에서 사용 가능한 국가 목록을 가져옵니다.
     */
    private List<CountryResponse> getAvailableCountries() {
        try {
            List<CountryResponse> countries = countryApiClient.getAvailableCountries();
            if (countries.isEmpty()) {
                throw new HolidayException(HolidayErrorCode.NO_COUNTRIES_FOUND);
            }
            return countries;
        } catch (Exception e) {
            log.error("국가 목록 API 호출 실패", e);
            throw new HolidayException(HolidayErrorCode.COUNTRY_API_FAILURE);
        }
    }

}
