package com.planitsquare.holiday.domain.country.client;

import com.planitsquare.holiday.domain.country.dto.CountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CountryApiClient {

    private static final String URL = "https://date.nager.at/api/v3/AvailableCountries";
    private final RestTemplate restTemplate;

    public List<CountryResponse> getAvailableCountries() {
        CountryResponse[] response = restTemplate.getForObject(URL, CountryResponse[].class);
        return Arrays.asList(response);
    }
}
