package com.planitsquare.holiday.domain.holiday.controller;

import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import com.terayukee.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holidays/sync")
@RequiredArgsConstructor
@Tag(name = "📡 Holiday 동기화", description = "공휴일 외부 데이터 동기화 API")
public class HolidaySyncController {

    private final HolidaySyncService holidaySyncService;

    @PostMapping
    @Operation(summary = "📥 특정 연도+국가의 공휴일 동기화", description = """
        외부 API에서 특정 연도와 국가에 대한 공휴일을 조회하여 저장합니다.
        실패 시 예외가 발생합니다.
    """)
    public CommonResponse<Void> syncByYearAndCountry(
            @Parameter(description = "연도", required = true, example = "2025")
            @RequestParam int year,
            @Parameter(description = "국가 코드 (ISO-2)", required = true, example = "KR")
            @RequestParam String countryCode
    ) {
        holidaySyncService.sync(year, countryCode);
        return CommonResponse.success();
    }

    @PostMapping("/year")
    @Operation(summary = "📥 특정 연도 전체 국가 동기화", description = """
        특정 연도에 대해 사용 가능한 모든 국가의 공휴일 데이터를 동기화합니다.
        예: 2025년의 모든 국가 공휴일
    """)
    public CommonResponse<Void> syncAllCountriesByYear(
            @Parameter(description = "연도", required = true, example = "2025")
            @RequestParam int year
    ) {
        holidaySyncService.syncByYear(year);
        return CommonResponse.success();
    }

    @PostMapping("/country")
    @Operation(summary = "📥 특정 국가 최근 5개년 동기화", description = """
        특정 국가에 대해 최근 5개년(현재 연도 포함)의 공휴일 데이터를 동기화합니다.
    """)
    public CommonResponse<Void> syncRecentYearsByCountry(
            @Parameter(description = "국가 코드 (ISO-2)", required = true, example = "KR")
            @RequestParam String countryCode
    ) {
        holidaySyncService.syncByCountry(countryCode);
        return CommonResponse.success();
    }

    @PostMapping("/bulk")
    @Operation(summary = "📥 전체 공휴일 대량 비동기 동기화", description = """
        모든 국가의 최근 5개년 공휴일 데이터를 비동기 방식으로 대량 동기화합니다.
        각 국가-연도 단위로 비동기 처리됩니다.
    """)
    public CommonResponse<Void> syncAll() {
        holidaySyncService.bulkSyncAll();
        return CommonResponse.success();
    }
}
