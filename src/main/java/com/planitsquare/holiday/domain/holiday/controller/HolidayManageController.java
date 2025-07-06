package com.planitsquare.holiday.domain.holiday.controller;

import com.planitsquare.holiday.domain.holiday.service.HolidaySyncService;
import com.terayukee.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
@Tag(name = "📦 Holiday 관리", description = "공휴일 데이터 삭제 및 갱신 API")
public class HolidayManageController {

    private final HolidaySyncService holidaySyncService;

    @DeleteMapping
    @Operation(summary = "🗑️ 공휴일 삭제", description = """
        특정 연도와 국가코드에 해당하는 공휴일 데이터를 모두 삭제합니다.
        
        ❗ API 호출 전 삭제 대상 데이터가 존재하는지 확인하세요.
        """)
    public CommonResponse<Void> deleteHoliday(
            @Parameter(description = "대상 연도", example = "2023", required = true)
            @RequestParam int year,
            @Parameter(description = "국가 코드 (ISO-2)", example = "KR", required = true)
            @RequestParam String countryCode
    ) {
        holidaySyncService.delete(year, countryCode);
        return CommonResponse.success();
    }

    @PutMapping("/refresh")
    @Operation(summary = "🔄 공휴일 갱신", description = """
        기존 연도+국가의 공휴일 데이터를 삭제한 뒤, 외부 API에서 최신 데이터로 재적재합니다.
        
        ✅ 자동화 스케줄러에서도 사용하는 내부 재처리 로직입니다.
        """)
    public CommonResponse<Void> refreshHoliday(
            @Parameter(description = "대상 연도", example = "2024", required = true)
            @RequestParam int year,
            @Parameter(description = "국가 코드 (ISO-2)", example = "US", required = true)
            @RequestParam String countryCode
    ) {
        holidaySyncService.refresh(year, countryCode);
        return CommonResponse.success();
    }
}
