package com.planitsquare.holiday.global.exception;

import com.planitsquare.holiday.domain.holiday.exception.HolidayException;
import com.terayukee.common.response.CommonResponse;
import com.terayukee.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.planitsquare.holiday")
public class HolidayExceptionHandler {

    @ExceptionHandler(HolidayException.class)
    public CommonResponse<Void> handleHolidayException(HolidayException e) {
        var code = e.getErrorCode();
        log.warn("❗ HolidayException 발생: [{}] {}", code.getStatus(), code.getMessage());

        return CommonResponse.fail(ErrorResponse.of(code.getMessage(), code.getStatus()));
    }
}
