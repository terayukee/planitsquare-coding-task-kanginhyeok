package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.holiday.dto.HolidaySearchRequest;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.exception.HolidayErrorCode;
import com.planitsquare.holiday.domain.holiday.exception.HolidayException;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidaySearchService {

    private final HolidayRepository holidayRepository;

    /**
     * 공휴일을 연도 및 국가코드를 기준으로 검색하고,
     * 선택적으로 날짜 범위와 공휴일 타입으로 필터링합니다.
     *
     * @param request 검색 조건을 포함한 요청 DTO
     * @return 조건에 부합하는 Holiday Page 결과
     */
    public Page<Holiday> search(HolidaySearchRequest request) {
        validateRequest(request);

        // 연도 + 국가코드 기준으로 1차 조회 (페이징)
        Page<Holiday> result = holidayRepository.searchByYearAndCountryCode(
                request.getYear(),
                request.getCountryCode(),
                request.toPageable()
        );

        if (result.isEmpty()) {
            throw new HolidayException(HolidayErrorCode.NO_HOLIDAYS_FOUND);
        }

        // 추가 필터링: 날짜 범위, 타입 등
        List<Holiday> filtered = result.stream()
                .filter(h -> {
                    if (request.hasDateFilter()) {
                        if (h.getDate().isBefore(request.getFrom()) || h.getDate().isAfter(request.getTo())) {
                            return false;
                        }
                    }

                    if (request.hasTypeFilter()) {
                        if (h.getTypes().stream().noneMatch(request.getTypes()::contains)) {
                            return false;
                        }
                    }

                    return true;
                })
                .toList();

        return new PageImpl<>(filtered, result.getPageable(), result.getTotalElements());
    }

    /**
     * 검색 요청의 필수 항목 유효성을 검사합니다.
     *
     * @param request HolidaySearchRequest
     */
    private void validateRequest(HolidaySearchRequest request) {
        if (request.getYear() > LocalDate.now().getYear()) {
            throw new HolidayException(HolidayErrorCode.INVALID_YEAR);
        }

        if (request.getCountryCode() == null || request.getCountryCode().isBlank()) {
            throw new HolidayException(HolidayErrorCode.INVALID_COUNTRY_CODE);
        }

        if (request.hasDateFilter() && request.getFrom().isAfter(request.getTo())) {
            throw new HolidayException(HolidayErrorCode.INVALID_DATE_RANGE);
        }
    }
}
