package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.tables.Time;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.List;

@Controller
public class TimeController {

    public long calculateEntriesTimeInMinutes(List<Time> list) {
        long completeTime = 0;
        for (Time time : list)
            completeTime = completeTime + Duration.between(time.getStart(), time.getEnd()).toMinutes();
        return completeTime;
    }

    public long calculateTimeBetweenEntriesInMinutes(List<Time> list) {
        long completeTime = 0;
        for (int i = 1; i < list.size(); i++)
            completeTime = completeTime + Duration.between(list.get(i - 1).getEnd(), list.get(i).getStart()).toMinutes();
        return completeTime;
    }
}
