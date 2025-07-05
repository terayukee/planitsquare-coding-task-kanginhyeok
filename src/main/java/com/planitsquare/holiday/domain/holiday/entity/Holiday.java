package com.planitsquare.holiday.domain.holiday.entity;

import com.planitsquare.holiday.domain.holiday.dto.PublicHolidayResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String localName;

    private String name;

    private String countryCode;

    private boolean fixed;

    private boolean global;

    @ElementCollection
    private List<String> counties;  // nullable: 지역별 공휴일일 경우

    private Integer launchYear;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> types;

    public static Holiday of(PublicHolidayResponse response) {
        return Holiday.builder()
                .date(response.getDate())
                .localName(response.getLocalName())
                .name(response.getName())
                .countryCode(response.getCountryCode())
                .fixed(response.isFixed())
                .global(response.isGlobal())
                .counties(response.getCounties())
                .launchYear(response.getLaunchYear())
                .types(response.getTypes())
                .build();
    }
}
