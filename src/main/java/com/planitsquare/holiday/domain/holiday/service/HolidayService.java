package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import com.planitsquare.holiday.domain.country.dto.CountryResponse;
import com.planitsquare.holiday.domain.holiday.client.PublicHolidayClient;
import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private static final int START_YEAR = 2020;
    private static final int END_YEAR = 2025;

    private final CountryApiClient countryApiClient;
    private final PublicHolidayClient publicHolidayClient;
    private final HolidayRepository holidayRepository;

    public void sync(int year, String countryCode) {
        List<PublicHolidayResponse> response = publicHolidayClient.getHolidays(year, countryCode);
        List<Holiday> holidays = response.stream()
                .map(r -> Holiday.builder()
                        .date(r.getDate())
                        .name(r.getName())
                        .localName(r.getLocalName())
                        .countryCode(r.getCountryCode())
                        .global(r.isGlobal())
                        .fixed(r.isFixed())
                        .types(r.getTypes())
                        .build()
                )
                .toList();
        holidayRepository.saveAll(holidays);
    }


    public void syncByYear(int year) {
        List<CountryResponse> countries = countryApiClient.getAvailableCountries();
        for (CountryResponse country : countries) {
            sync(year, country.getCountryCode());
        }
    }

    public void syncByCountry(String countryCode) {
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            sync(year, countryCode);
        }
    }

    public void bulkSyncAll() {
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            syncByYear(year);
        }
    }
}
