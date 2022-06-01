package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import com.hwr.hwrzeiterfassung.response.models.TimeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Times
 */
@Controller
@RequestMapping(path = "/time")
public class TimeController {
    @Autowired
    private LoginController loginController;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private DayRepository dayRepository;

    /**
     * calculate the duration of the times in a list
     *
     * @param list List of Times
     * @return duration of the times
     */
    public long calculateEntriesTimeInMinutes(List<Time> list) {
        long completeTime = 0;
        for (Time time : list)
            completeTime = completeTime + Duration.between(time.getStart(), time.getEnd()).toMinutes();
        return completeTime;
    }

    /**
     * calculate the duration between the times in a list
     *
     * @param list List of Times
     * @return duration between the times
     */
    public long calculateTimeBetweenEntriesInMinutes(List<Time> list) {
        long completeTime = 0;
        for (int i = 1; i < list.size(); i++)
            completeTime = completeTime + Duration.between(list.get(i - 1).getEnd(), list.get(i).getStart()).toMinutes();
        return completeTime;
    }

    /**
     * calculate the pause time with tow list from one day
     *
     * @param day the day for them the pause is calculated
     * @return pause time
     */
    public double calculatePauseTime(Day day) {
        double pause = 0;
        pause += calculateEntriesTimeInMinutes(timeRepository.findAllByDayAndPause(day, true));
        pause += calculateTimeBetweenEntriesInMinutes(timeRepository.findAllByDayAndPause(day, false));
        pause /= 60;
        return pause;
    }

    /**
     * calculate the Work Time of a day
     *
     * @param day the Day
     * @return work time
     */
    public double calculateWorkTime(Day day) {
        return (calculateEntriesTimeInMinutes(timeRepository.findAllByDayAndPause(day, false)) / (double) 60) - day.getTargetDailyWorkingTime();
    }

    /**
     * correct the pause time of a day
     *
     * @param day day to correct pause time
     */
    public void correctPauseTime(Day day) {
        var workTime = day.getTargetDailyWorkingTime();
        var pauseTime = day.getPauseTime();
        var minPause = getMinPause(workTime);

        if (minPause > pauseTime) {
            var timeList = timeRepository.findAllByDayAndPause(day, false);
            var vipPause = minPause;
            for (int i = timeList.size() - 1; i > 0; i--) {
                var time = timeList.get(i);
                double dur = Duration.between(time.getStart(), time.getEnd()).toSeconds();
                dur /= 3600;

                if (dur == vipPause) {
                    time.setPause(true);
                }
                if (dur > vipPause) {
                    var diff = (long) Math.ceil(dur - vipPause);
                    var newEnd = time.getEnd().minusMinutes(diff);
                    var timePause = new Time(newEnd, time.getEnd(), true, time.getNote(), time.getDay(), time.getProject());
                    timeRepository.saveAndFlush(timePause);
                    time.setEnd(newEnd);
                    timeRepository.saveAndFlush(time);
                }
                if (dur < vipPause) {
                    vipPause -= dur;
                    time.setPause(true);
                    timeRepository.saveAndFlush(time);
                }
            }
            day.setPauseTime(minPause);
        }
    }

    /**
     * get the minimal pause time in hours according to the ArbZG
     *
     * @param workTime the working time of the day
     * @return the minimal pause
     */
    public double getMinPause(double workTime) {
        if (workTime > 10)
            return 1;

        if (workTime > 9)
            return (double) 45 / 60;

        if (workTime > 6)
            return (double) 30 / 60;
        return 0;
    }


    /**
     * get the last Time status of a human
     *
     * @param email    email for login validation
     * @param password hashed password for login validation
     * @return Optional of Time Action
     */
    @GetMapping(path = "/lastStatus")
    public @ResponseBody
    Optional<TimeAction> isTimeStatusToBookStart(@RequestParam String email, @RequestParam String password) {
        loginController.validateLoginInformation(email, password);


        var day = dayRepository.findAllByDateAndHuman_Email(LocalDate.now(), email);
        if (day.isEmpty())
            return Optional.empty();

        var times = timeRepository.findAllByDay(day.get(day.size() - 1));
        if (times.isEmpty()) {
            //Start am Vortag
            var lastDays = dayRepository.findAllByDateAndHuman_Email(LocalDate.now().minusDays(1), email);
            if (lastDays.isEmpty())
                return Optional.empty();

            var lastTimes = timeRepository.findAllByDay(day.get(day.size() - 1));

            if (lastTimes.isEmpty())
                return Optional.empty();

            var lastTime = lastTimes.get(lastTimes.size() - 1);
            if (lastTime.getEnd() == null)
                return Optional.of(new TimeAction(false, lastTime.isPause(), lastTime.getProject().getId()));

            return Optional.empty();
        }
        var time = times.get(times.size() - 1);
        if (time.getEnd() == null)
            return Optional.of(new TimeAction(true, time.isPause(), time.getProject().getId()));
        return Optional.of(new TimeAction(false, time.isPause(), time.getProject().getId()));
    }

}
