package com.planitsquare.holiday.domain.holiday.converter;

import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;

import java.util.List;

public interface HolidayConverter {
    List<Holiday> convert(List<PublicHolidayResponse> responses);
}
