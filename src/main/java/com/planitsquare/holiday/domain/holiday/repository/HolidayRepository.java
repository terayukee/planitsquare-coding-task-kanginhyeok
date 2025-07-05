package com.planitsquare.holiday.domain.holiday.repository;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByCountryCodeAndDateBetween(String countryCode, LocalDate start, LocalDate end);

    default List<Holiday> findByYearAndCountryCode(int year, String countryCode) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return findByCountryCodeAndDateBetween(countryCode, start, end);
    }
}
