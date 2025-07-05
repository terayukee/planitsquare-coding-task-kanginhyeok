package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.holiday.client.PublicHolidayClient;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    private final PublicHolidayClient client;
    private final HolidayRepository repository;

    public HolidayService(PublicHolidayClient client, HolidayRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    public void sync(int year, String countryCode) {
        List<Holiday> holidays = client.getHolidays(year, countryCode);
        repository.saveAll(holidays);
    }
}
