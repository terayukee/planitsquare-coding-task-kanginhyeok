package com.planitsquare.holiday.domain.holiday.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class HolidaySearchRequest {
    private Integer year;
    private String countryCode;
    private LocalDate from;
    private LocalDate to;
    private List<String> types;
    private int page;
    private int size;
}
