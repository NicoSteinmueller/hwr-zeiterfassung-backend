package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.DayController;
import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewCompact;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping(path = "/overview")
public class OverviewController {
    @Autowired
    private LoginController loginController;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private DayController dayController;

    @GetMapping(path = "/DayFullOverviewInInterval")
    public @ResponseBody
    Iterable<DayOverviewFull> getDayFullOverviewsInInterval(@RequestParam String email, @RequestParam String password, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        if (!loginController.validateLoginInformation(email, password))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");

        var days = dayRepository.findAllByDateBetweenAndHuman_Email(start, end, email);

        if (days.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No days found for the user in the interval");

        var list = new ArrayList<DayOverviewFull>();
        for (var day : days) {
            var times = timeRepository.findAllByDay(day);
            var workingTime = dayController.calculateWorkingTime(day);
            if (times.isEmpty()) {
                list.add(new DayOverviewFull(day.getDate(), null, null, day.getPauseTime(), workingTime));
                continue;
            }

            LocalDateTime dayStart = LocalDateTime.MAX;
            LocalDateTime dayEnd = LocalDateTime.MIN;

            for (int i = 0; i < times.size(); i++) {
                var time = times.get(i);
                if (time.getStart() != null && dayStart.isAfter(time.getStart()))
                    dayStart = time.getStart();
                if (time.getEnd() != null && dayEnd.isBefore(time.getEnd()))
                    dayEnd = time.getEnd();
            }
            if (dayStart == LocalDateTime.MAX)
                dayStart = null;
            if (dayEnd == LocalDateTime.MIN)
                dayEnd = null;
            list.add(new DayOverviewFull(day.getDate(), dayStart, dayEnd, day.getPauseTime(), workingTime));

        }
        return list;
    }

    @GetMapping(path = "/DayCompactOverviewInInterval")
    public @ResponseBody
    Iterable<DayOverviewCompact> getDayCompactOverviewsInInterval(@RequestParam String email, @RequestParam String password, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        if (!loginController.validateLoginInformation(email, password))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");

        var days = dayRepository.findAllByDateBetweenAndHuman_Email(start, end, email);

        if (days.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No days found for the user in the interval");

        var list = new ArrayList<DayOverviewCompact>();
        for (var day : days) {
            var workingTime = dayController.calculateWorkingTime(day);
            list.add(new DayOverviewCompact(day.getDate(), day.getPauseTime(), workingTime));

        }
        return list;
    }

    @GetMapping(path = "/AverageWorkingHoursInInterval")
    public @ResponseBody
    double getAverageWorkingHoursInInterval(@RequestParam String email, @RequestParam String password, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        if (!loginController.validateLoginInformation(email, password))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");

        Optional<Double> hours = dayRepository.getWorkingTimeAverageByDateBetweenAndHuman_Email(start, end, email);

        if (hours.isEmpty())
            return 0;
        return hours.get();
    }

    @GetMapping(path = "/AveragePauseInInterval")
    public @ResponseBody
    double getAveragePauseInInterval(@RequestParam String email, @RequestParam String password) {

        if (!loginController.validateLoginInformation(email, password))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");

        Optional<Double> hours = dayRepository.getPauseAverageByDateBetweenAndHuman_Email(LocalDate.now().minusDays(10), LocalDate.now(), email);
        if (hours.isEmpty())
            return 0;
        return hours.get();
    }

}
