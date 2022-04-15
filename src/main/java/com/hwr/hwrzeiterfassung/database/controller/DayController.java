package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * Controller for the Day
 */
@Controller
public class DayController {
    /**
     * calculates the time worked on this day
     *
     * @param day the day for calculate the time
     * @return the working time for the day
     */
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
