package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Day overview compact.
 */
@AllArgsConstructor
@Data
public class DayOverviewCompact {
    private LocalDate date;
    private Double pause;
    private Double workTime;
}
