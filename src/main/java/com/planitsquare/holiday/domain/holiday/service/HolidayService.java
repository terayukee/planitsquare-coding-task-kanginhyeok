package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import com.planitsquare.holiday.domain.country.dto.CountryResponse;
import com.planitsquare.holiday.domain.holiday.client.HolidayApiClient;
import com.planitsquare.holiday.domain.holiday.converter.HolidayConverter;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    private final HolidayApiClient apiClient;
    private final HolidayRepository repository;
    private final HolidayConverter converter;
    private final CountryApiClient countryApiClient;

    private static final int START_YEAR = 2020;
    private static final int END_YEAR = 2025;

    public HolidayService(HolidayApiClient apiClient, HolidayRepository repository, HolidayConverter converter, CountryApiClient countryApiClient) {
        this.apiClient = apiClient;
        this.repository = repository;
        this.converter = converter;
        this.countryApiClient = countryApiClient;
    }

    public void sync(int year, String countryCode) {
        var responses = apiClient.getHolidays(year, countryCode);
        var holidays = converter.convert(responses);
        repository.saveAll(holidays);
    }

    public void bulkSyncAll() {
        List<CountryResponse> countries = countryApiClient.getAvailableCountries();

        for (int year = START_YEAR; year <= END_YEAR; year++) {
            for (CountryResponse country : countries) {
                this.sync(year, country.getCountryCode());
            }
        }
    }
}
