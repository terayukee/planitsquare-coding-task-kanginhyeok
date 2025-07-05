package com.planitsquare.holiday.domain.country;

import com.planitsquare.holiday.domain.country.client.CountryApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CountryApiClientTest {

    @Autowired
    private CountryApiClient countryApiClient;

    @Test
    void 국가_목록을_정상적으로_가져온다() {
        var countries = countryApiClient.getAvailableCountries();
        assertFalse(countries.isEmpty());
        System.out.println(countries.get(0).getCountryCode() + " / " + countries.get(0).getName());
    }
}
