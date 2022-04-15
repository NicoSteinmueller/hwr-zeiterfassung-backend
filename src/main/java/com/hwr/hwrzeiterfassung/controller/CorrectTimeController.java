package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.controller.TimeController;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import com.hwr.hwrzeiterfassung.response.models.DateAndListOfTimes;
import com.hwr.hwrzeiterfassung.response.models.DayAndListOfTimes;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewCompact;
import com.hwr.hwrzeiterfassung.response.models.TimeCompact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for correct times
 */
@Controller
@RequestMapping(path = "/correct")
public class CorrectTimeController {
    @Autowired
    private LoginController loginController;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private TimeController timeController;

    /**
     * get the information's and times of a day
     *
     * @param email    email for login validation
     * @param password hashed password for login validation
     * @param date     the date from the day
     * @return Optional of DayAndListOfTimes
     */
    @GetMapping(path = "/getDayInformationAndTimes")
    public @ResponseBody Optional<DayAndListOfTimes> getDayInformationAndTimes(@RequestParam String email, @RequestParam String password, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        loginController.validateLoginInformation(email, password);
        var days = dayRepository.findAllByDateAndHuman_Email(date, email);

        if (days.isEmpty())
            return Optional.empty();

        var day = days.get(0);
        var dayOverviewCompact = new DayOverviewCompact(day.getDate(), day.getPauseTime(), day.getWorkingTimeDifference() + day.getTargetDailyWorkingTime());

        var times = timeRepository.findAllByDay(day);
        if (times.isEmpty())
            return Optional.of(new DayAndListOfTimes(dayOverviewCompact, null));

        List<TimeCompact> timeCompactList = new ArrayList<>();

        for (var time : times)
            timeCompactList.add(new TimeCompact(time.getStart(), time.getEnd(), time.isPause(), time.getNote(), time.getProject()));

        return Optional.of(new DayAndListOfTimes(dayOverviewCompact, timeCompactList));
    }

    /**
     * change the times of a day
     *
     * @param email                  email for login validation
     * @param password               hashed password for login validation
     * @param dateAndListOfTimes     the date of the day and a list of the new times for the day
     * @param targetDailyWorkingTime the target working time for this day
     * @return HttpStatus Accepted or Not_Acceptable
     */
    @PostMapping(path = "/times")
    public @ResponseStatus
    HttpStatus changeDayTimes(@RequestParam String email, @RequestParam String password, @RequestParam DateAndListOfTimes dateAndListOfTimes, @RequestParam double targetDailyWorkingTime) {
        loginController.validateLoginInformation(email, password);

        var days = dayRepository.findAllByDateAndHuman_Email(dateAndListOfTimes.getDate(), email);
        Day day;
        if (days.isEmpty())
            day = new Day();
        else
            day = days.get(0);

        day.setTargetDailyWorkingTime(targetDailyWorkingTime);

        List<Time> times = new ArrayList<>();
        for (var timeInput : dateAndListOfTimes.getTimes())
            times.add(new Time(timeInput.getStart(), timeInput.getEnd(), timeInput.isPause(), timeInput.getNote(), day, timeInput.getProject()));

        dayRepository.saveAndFlush(day);
        timeRepository.deleteAllByDay(day);
        timeRepository.saveAllAndFlush(times);

        day.setPauseTime(timeController.calculatePauseTime(day));
        var workTime = timeController.calculateEntriesTimeInMinutes(timeRepository.findAllByDayAndPause(day, false)) / (double) 60;
        day.setWorkingTimeDifference(workTime - day.getTargetDailyWorkingTime());
        dayRepository.saveAndFlush(day);
        return HttpStatus.ACCEPTED;
    }

}
