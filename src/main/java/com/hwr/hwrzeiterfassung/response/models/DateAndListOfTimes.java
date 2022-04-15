package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Date and list of times.
 */
@AllArgsConstructor
@Data
public class DateAndListOfTimes {
    private LocalDate date;
    private List<TimeCompact> times;
}
