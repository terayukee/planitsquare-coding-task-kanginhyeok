package com.planitsquare.holiday.domain.holiday.repository;

import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByCountryCodeAndDateBetween(String countryCode, LocalDate start, LocalDate end);

    default List<Holiday> findByYearAndCountryCode(int year, String countryCode) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return findByCountryCodeAndDateBetween(countryCode, start, end);
    }

    @Query("""
    SELECT h FROM Holiday h
    WHERE YEAR(h.date) = :year AND h.countryCode = :countryCode
""")
    Page<Holiday> searchByYearAndCountryCode(
            @Param("year") int year,
            @Param("countryCode") String countryCode,
            Pageable pageable
    );

    @Modifying
    @Query("DELETE FROM Holiday h WHERE YEAR(h.date) = :year AND h.countryCode = :countryCode")
    void deleteByYearAndCountryCode(@Param("year") int year, @Param("countryCode") String countryCode);

}
