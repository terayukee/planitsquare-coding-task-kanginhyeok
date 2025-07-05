package com.planitsquare.holiday.domain.holiday;

import com.planitsquare.holiday.domain.holiday.dto.HolidaySearchRequest;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import com.planitsquare.holiday.domain.holiday.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HolidaySearchTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @BeforeEach
    void setUp() {
        holidayRepository.deleteAll(); // 🔄 기존 데이터 초기화

        holidayRepository.save(
                Holiday.builder()
                        .date(LocalDate.of(2025, 12, 25))
                        .name("Christmas Day")
                        .localName("크리스마스")
                        .countryCode("KR")
                        .global(true)
                        .fixed(false)
                        .types(List.of("Public", "Religious"))
                        .build()
        );
    }

    @Test
    void 연도_국가_기간_타입_기반_공휴일_검색() {
        // given
        HolidaySearchRequest request = HolidaySearchRequest.builder()
                .year(2025)
                .countryCode("KR")
                .from(LocalDate.of(2025, 1, 1))
                .to(LocalDate.of(2025, 12, 31))
                .types(List.of("Public")) // ✅ types 필터링
                .page(0)
                .size(10)
                .build();

        // when
        Page<Holiday> result = holidayService.search(request);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty(), "검색 결과는 비어있지 않아야 합니다."),
                () -> assertEquals("KR", result.getContent().get(0).getCountryCode()),
                () -> assertTrue(result.getContent().get(0).getTypes().contains("Public"))
        );
    }
}
