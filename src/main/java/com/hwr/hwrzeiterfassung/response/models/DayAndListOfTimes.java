package com.hwr.hwrzeiterfassung.response.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DayAndListOfTimes {
    private DayOverviewCompact dayOverviewCompact;
    private List<TimeCompact> times;
}
