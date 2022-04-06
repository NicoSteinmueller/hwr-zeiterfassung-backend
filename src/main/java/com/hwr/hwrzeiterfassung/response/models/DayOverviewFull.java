package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class DayOverviewFull {
    private LocalDate date;
    private LocalDateTime start;
    private LocalDateTime end;
    private Double pause;
    private Double workTime;
}
