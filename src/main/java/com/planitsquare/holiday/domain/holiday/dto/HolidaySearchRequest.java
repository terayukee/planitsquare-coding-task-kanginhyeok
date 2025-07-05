package com.planitsquare.holiday.domain.holiday.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class HolidaySearchRequest {

    private int year;
    private String countryCode;
    private LocalDate from;
    private LocalDate to;
    private List<String> types;

    private int page;
    private int size;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }

    public boolean hasDateFilter() {
        return from != null && to != null;
    }

    public boolean hasTypeFilter() {
        return types != null && !types.isEmpty();
    }
}
