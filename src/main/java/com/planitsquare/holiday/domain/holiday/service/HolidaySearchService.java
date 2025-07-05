package com.planitsquare.holiday.domain.holiday.service;

import com.planitsquare.holiday.domain.holiday.dto.HolidaySearchRequest;
import com.planitsquare.holiday.domain.holiday.entity.Holiday;
import com.planitsquare.holiday.domain.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidaySearchService {

    private final HolidayRepository holidayRepository;

    public Page<Holiday> search(HolidaySearchRequest request) {
        Page<Holiday> result = holidayRepository.searchByYearAndCountryCode(
                request.getYear(),
                request.getCountryCode(),
                request.toPageable()
        );

        // 추가 필터링
        List<Holiday> filtered = result.stream()
                .filter(h -> {
                    if (request.hasDateFilter()) {
                        if (h.getDate().isBefore(request.getFrom()) || h.getDate().isAfter(request.getTo())) {
                            return false;
                        }
                    }

                    if (request.hasTypeFilter()) {
                        if (h.getTypes().stream().noneMatch(request.getTypes()::contains)) {
                            return false;
                        }
                    }

                    return true;
                })
                .toList();

        return new PageImpl<>(filtered, result.getPageable(), result.getTotalElements());
    }
}
