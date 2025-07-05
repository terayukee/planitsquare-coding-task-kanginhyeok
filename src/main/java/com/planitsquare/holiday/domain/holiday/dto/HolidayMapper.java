package com.planitsquare.holiday.domain.holiday.dto;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;

public class HolidayMapper {

    public static Holiday toEntity(PublicHolidayResponse dto) {
        Holiday holiday = new Holiday();
        holiday.setDate(dto.getDate());
        holiday.setName(dto.getName());
        holiday.setLocalName(dto.getLocalName());
        holiday.setCountryCode(dto.getCountryCode());
        holiday.setGlobal(dto.isGlobal());
        return holiday;
    }
}
