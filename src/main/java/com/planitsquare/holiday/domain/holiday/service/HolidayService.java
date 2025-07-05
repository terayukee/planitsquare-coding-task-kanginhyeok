package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.holiday.client.HolidayApiClient;
import com.planitsquare.holiday.domain.holiday.dto.HolidayMapper;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    private final HolidayApiClient apiClient;
    private final HolidayRepository repository;

    public HolidayService(HolidayApiClient apiClient, HolidayRepository repository) {
        this.apiClient = apiClient;
        this.repository = repository;
    }

    public void sync(int year, String countryCode) {
        List<Holiday> holidays = apiClient.getHolidays(year, countryCode)
                .stream()
                .map(HolidayMapper::toEntity)
                .toList();

        repository.saveAll(holidays);
    }
}
