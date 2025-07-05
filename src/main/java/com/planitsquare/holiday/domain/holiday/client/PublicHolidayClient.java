package com.planitsquare.holiday.domain.holiday.client;

import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class PublicHolidayClient implements HolidayApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<PublicHolidayResponse> getHolidays(int year, String countryCode) {
        String url = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode;
        PublicHolidayResponse[] response = restTemplate.getForObject(url, PublicHolidayResponse[].class);
        return Arrays.asList(response);
    }

}
