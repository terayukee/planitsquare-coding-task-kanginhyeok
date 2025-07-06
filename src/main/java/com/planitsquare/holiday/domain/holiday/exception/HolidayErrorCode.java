package com.planitsquare.holiday.domain.holiday.exception;

import com.terayukee.common.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HolidayErrorCode implements ErrorCode {

    INVALID_YEAR(400, "유효하지 않은 연도입니다."),
    INVALID_COUNTRY_CODE(400, "국가 코드가 비어있거나 유효하지 않습니다."),
    INVALID_DATE_RANGE(400, "날짜 범위가 올바르지 않습니다."),
    NO_HOLIDAYS_FOUND(404, "해당 조건에 맞는 공휴일 데이터가 없습니다."),
    SYNC_FAILED(500, "공휴일 동기화에 실패했습니다."),
    COUNTRY_API_FAILURE(502, "국가 목록 API 호출에 실패했습니다."),
    HOLIDAY_API_FAILURE(502, "공휴일 API 호출에 실패했습니다."),
    DATA_SAVE_FAILED(500, "공휴일 저장에 실패했습니다."),
    NO_COUNTRIES_FOUND(404, "사용 가능한 국가 목록이 없습니다."),
    EMPTY_HOLIDAY_DATA(204, "해당 연도 및 국가의 공휴일 정보가 없습니다.");

    private final int status;
    private final String message;
}
