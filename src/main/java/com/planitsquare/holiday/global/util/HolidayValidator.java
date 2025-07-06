package com.planitsquare.holiday.global.util;

import com.planitsquare.holiday.domain.holiday.exception.HolidayErrorCode;
import com.planitsquare.holiday.domain.holiday.exception.HolidayException;

import java.time.LocalDate;

public class HolidayValidator {

    public static void validateYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year > currentYear) {
            throw new HolidayException(HolidayErrorCode.INVALID_YEAR);
        }
    }

}
