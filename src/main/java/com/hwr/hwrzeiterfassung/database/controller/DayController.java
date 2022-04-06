package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class DayController {

    public double calculateWorkingTime(Day day) {
        double workingTime = 0;
        Double workingTimeTarget = day.getTargetDailyWorkingTime();
        Double workingTimeDifference = day.getWorkingTimeDifference();
        if (Objects.isNull(workingTimeTarget)) {
            workingTime += 0;
        } else {
            workingTime += workingTimeTarget;
        }
        if (Objects.isNull(workingTimeDifference)) {
            workingTime += 0;
        } else {
            workingTime += workingTimeDifference;
        }
        return workingTime;
    }
}
