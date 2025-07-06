package com.planitsquare.holiday.domain.holiday.controller;

import com.planitsquare.holiday.domain.holiday.dto.HolidaySearchRequest;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.service.HolidaySearchService;
import com.terayukee.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "📅 Holiday API", description = "공휴일 검색 및 관리 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidaySearchService holidaySearchService;

    @GetMapping("/holidays")
    @Operation(
            summary = "📅 공휴일 검색",
            description = """
    연도, 국가코드, 기간, 공휴일 타입을 기준으로 공휴일 목록을 검색합니다.  
    - `year`와 `countryCode`는 필수입니다.  
    - `from`, `to`는 기간 필터 (옵션)입니다.  
    - `types`는 공휴일 타입 (예: Public, Bank 등) 필터입니다.
    """
    )
    public CommonResponse<Page<Holiday>> searchHolidays(
            @Parameter(description = "검색할 연도 (예: 2025)", example = "2025", required = true)
            @RequestParam int year,

            @Parameter(description = "국가 코드 (ISO2 형식, 예: KR, US)", example = "KR", required = true)
            @RequestParam String countryCode,

            @Parameter(description = "검색 시작일 (yyyy-MM-dd)", example = "2025-01-01", required = false)
            @RequestParam(required = false) LocalDate from,

            @Parameter(description = "검색 종료일 (yyyy-MM-dd)", example = "2025-12-31", required = false)
            @RequestParam(required = false) LocalDate to,

            @Parameter(description = "공휴일 타입 리스트 (예: Public, Bank)", example = "[\"Public\"]", required = false)
            @RequestParam(required = false) List<String> types,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0", required = false)
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "10", required = false)
            @RequestParam(defaultValue = "10") int size
    ) {
        HolidaySearchRequest request = HolidaySearchRequest.builder()
                .year(year)
                .countryCode(countryCode)
                .from(from)
                .to(to)
                .types(types)
                .page(page)
                .size(size)
                .build();

        return CommonResponse.success(holidaySearchService.search(request));
    }

}
