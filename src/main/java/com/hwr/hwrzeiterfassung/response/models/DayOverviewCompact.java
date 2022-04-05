package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class DayOverviewCompact {
    private LocalDate date;
    private double pause;
    private double workTime;
}
