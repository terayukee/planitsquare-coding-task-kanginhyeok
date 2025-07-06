package com.planitsquare.holiday.domain.holiday.exception;

import com.terayukee.common.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HolidayErrorCode implements ErrorCode {

    HOLIDAY_NOT_FOUND(404, "해당 공휴일 정보를 찾을 수 없습니다."),
    INVALID_YEAR_RANGE(400, "연도 범위가 유효하지 않습니다."),
    COUNTRY_CODE_REQUIRED(400, "국가 코드(countryCode)는 필수 입력값입니다."),
    SYNC_FAILED(500, "공휴일 동기화에 실패했습니다.");

    private final int status;
    private final String message;
}
