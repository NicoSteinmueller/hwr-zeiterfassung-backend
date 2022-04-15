package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

/**
 * Controller for the Day
 */
@Controller
public class DayController {
    @Autowired
    private DayRepository dayRepository;

    /**
     * get the day with the date from the human or if no day with this date exits for the human, then create a new day
     *
     * @param date  date of the day
     * @param human human of the day
     * @return Day
     */
    public Day getDayOrCreateDayByDateAndHuman(LocalDate date, Human human) {
        var dayList = dayRepository.findAllByDateAndHuman_Email(date, human.getEmail());
        if (dayList.isEmpty()) {
            dayRepository.saveAndFlush(new Day(date, human.getTargetDailyWorkingTime(), human));
            dayList = dayRepository.findAllByDateAndHuman_Email(date, human.getEmail());
        }
        return dayList.get(0);
    }

}
