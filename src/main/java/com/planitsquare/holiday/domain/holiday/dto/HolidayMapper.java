package com.planitsquare.holiday.domain.holiday.dto;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;

public class HolidayMapper {

    public static Holiday toEntity(PublicHolidayResponse dto) {
        return Holiday.builder()
                .date(dto.getDate())
                .name(dto.getName())
                .localName(dto.getLocalName())
                .countryCode(dto.getCountryCode())
                .global(dto.isGlobal())
                .fixed(dto.isFixed())
                .types(dto.getTypes())
                .build();
    }
}
