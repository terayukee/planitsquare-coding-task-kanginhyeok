package com.planitsquare.holiday.domain.holiday.client;

import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;

import java.util.List;

public interface HolidayApiClient {

    List<PublicHolidayResponse> getHolidays(int year, String countryCode);
}
