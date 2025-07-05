package com.planitsquare.holiday.domain.holiday.client;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class PublicHolidayClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Holiday> getHolidays(int year, String countryCode) {
        String url = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode;
        Holiday[] response = restTemplate.getForObject(url, Holiday[].class);
        return Arrays.asList(response);
    }
}
