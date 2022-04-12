package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
public class DateAndListOfTimes {
    private LocalDate date;
    private List<TimeCompact> times;
}
