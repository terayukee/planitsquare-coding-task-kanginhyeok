package com.planitsquare.holiday.domain.holiday.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PublicHolidayResponse {
    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean global;
}
