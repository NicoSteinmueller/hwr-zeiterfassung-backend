package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.*;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Controller for book all times
 */
@Controller
@RequestMapping(path = "/book")
public class BookController {
    @Autowired
    private LoginController loginController;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private DayController dayController;
    @Autowired
    private ProjectController projectController;
    @Autowired
    private HumanController humanController;
    @Autowired
    private TimeController timeController;

    /**
     * add a time entry with the current time to the DB
     *
     * @param email     email for login validation
     * @param password  hashed password for login validation
     * @param isStart   if the time to book is a start of an entry
     * @param pause     if the time is a pause
     * @param note      a note for the entry
     * @param projectId the project associated with the entry
     * @return Http Status Accepted / Not Acceptable
     */
    @PostMapping(path = "/time")
    public ResponseEntity<HttpStatus> addTimeEntry(@RequestParam String email, @RequestParam String password,
                                                   @RequestParam boolean isStart, @RequestParam boolean pause, @RequestParam String note, @RequestParam int projectId) {
        loginController.validateLoginInformation(email, password);

        var datetime = LocalDateTime.now();
        var human = humanController.getHumanByEmail(email);
        var project = projectController.getProjectById(projectId);
        var day = dayController.getDayOrCreateDayByDateAndHuman(datetime.toLocalDate(), human);

        var time = getLastTimeFromDay(datetime.toLocalDate(), email, pause);

        if (time.isPresent() && time.get().getEnd() == null) {
            if (isStart)
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Endtime for the previous Booking on this day.");
            else
                bookEndTime(time.get(), day, datetime);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        if (isStart) {
            timeRepository.saveAndFlush(new Time(datetime, null, pause, note, day, project));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } else {
            var lastTime = getLastTimeFromDay(datetime.toLocalDate().minusDays(1), email, pause);

            if (lastTime.isPresent() && lastTime.get().getEnd() == null) {
                var lastDay = lastTime.get().getDay();
                bookEndTime(lastTime.get(), lastDay, LocalDateTime.of(lastDay.getDate(), LocalTime.MAX));
                timeRepository.saveAndFlush(new Time(LocalDateTime.of(day.getDate(), LocalTime.MIDNIGHT), datetime, pause, note, day, project));
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Starttime for the Booking.");
        }
    }

    /**
     * book the End Time for a Time
     *
     * @param time     time to book the end time
     * @param day      day from the time
     * @param dateTime the date Time to book
     */
    public void bookEndTime(Time time, Day day, LocalDateTime dateTime) {
        time.setEnd(dateTime);
        timeRepository.saveAndFlush(time);
        day.setWorkingTimeDifference(timeController.calculateWorkTime(day));
        day.setPauseTime(timeController.calculatePauseTime(day));
        timeController.correctPauseTime(day);
        dayRepository.saveAndFlush(day);
    }

    /**
     * get the last Time entry from the date
     *
     * @param date  the date to get the time
     * @param email the email of the human
     * @param pause if the time to get is a pause
     * @return Optional of Time
     */
    public Optional<Time> getLastTimeFromDay(LocalDate date, String email, boolean pause) {
        var lastDays = dayRepository.findAllByDateAndHuman_Email(date, email);

        if (!lastDays.isEmpty()) {
            var lastDay = lastDays.get(0);
            var lastTimes = timeRepository.findAllByDayAndPause(lastDay, pause);

            if (!lastTimes.isEmpty())
                return Optional.of(lastTimes.get(lastDays.size() - 1));
        }
        return Optional.empty();
    }
}
