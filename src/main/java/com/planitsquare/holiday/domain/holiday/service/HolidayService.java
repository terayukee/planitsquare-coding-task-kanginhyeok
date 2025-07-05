package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.holiday.client.HolidayApiClient;
import com.planitsquare.holiday.domain.holiday.converter.HolidayConverter;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import org.springframework.stereotype.Service;

@Service
public class HolidayService {

    private final HolidayApiClient apiClient;
    private final HolidayRepository repository;
    private final HolidayConverter converter;

    public HolidayService(HolidayApiClient apiClient, HolidayRepository repository, HolidayConverter converter) {
        this.apiClient = apiClient;
        this.repository = repository;
        this.converter = converter;
    }

    public void sync(int year, String countryCode) {
        var responses = apiClient.getHolidays(year, countryCode);
        var holidays = converter.convert(responses);
        repository.saveAll(holidays);
    }
}
