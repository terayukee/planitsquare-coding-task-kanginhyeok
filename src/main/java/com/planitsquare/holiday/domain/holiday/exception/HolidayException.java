package com.planitsquare.holiday.domain.holiday.exception;

import com.terayukee.common.exception.BusinessException;

public class HolidayException extends BusinessException {

    public HolidayException(HolidayErrorCode errorCode) {
        super(errorCode);
    }
}
