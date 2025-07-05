package com.planitsquare.holiday.domain.holiday.converter;

import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultHolidayConverter implements HolidayConverter {

    @Override
    public List<Holiday> convert(List<PublicHolidayResponse> responses) {
        return responses.stream()
                .map(response -> Holiday.builder()
                        .date(response.getDate())
                        .name(response.getName())
                        .localName(response.getLocalName())
                        .countryCode(response.getCountryCode())
                        .global(response.isGlobal())
                        .fixed(response.isFixed())
                        .types(response.getTypes())
                        .build()
                )
                .toList();
    }
}
