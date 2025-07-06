package com.planitsquare.holiday.domain.holiday.controller;

import com.planitsquare.holiday.domain.holiday.dto.HolidaySearchRequest;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.service.HolidaySearchService;
import com.terayukee.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
@Tag(name = "📦 Holiday 검색", description = "공휴일 데이터 검색 API")
public class HolidaySearchController {

    private final HolidaySearchService holidaySearchService;

    @GetMapping
    @Operation(
            summary = "🔍 공휴일 검색",
            description = """
            연도와 국가코드를 기준으로 공휴일 목록을 조회합니다.

            ✅ 선택적으로 날짜 범위 (`from`, `to`)와 공휴일 유형 (`types`)도 필터링할 수 있습니다.  
            ✅ 결과는 페이징 형태로 응답됩니다.
        """,
            parameters = {
                    @Parameter(name = "year", description = "검색할 연도 (필수)", required = true, example = "2025"),
                    @Parameter(name = "countryCode", description = "ISO 2자리 국가코드 (필수)", required = true, example = "KR"),
                    @Parameter(name = "from", description = "검색 시작일 (yyyy-MM-dd)", example = "2025-01-01"),
                    @Parameter(name = "to", description = "검색 종료일 (yyyy-MM-dd)", example = "2025-12-31"),
                    @Parameter(name = "types", description = "공휴일 유형 필터 (복수 가능)", example = "Public"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "페이지 당 데이터 수", example = "10", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "공휴일 검색 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonResponse.class)
                            )
                    )
            }
    )
    public CommonResponse<Page<Holiday>> searchHolidays(HolidaySearchRequest request) {
        Page<Holiday> result = holidaySearchService.search(request);
        return CommonResponse.success(result);
    }
}
