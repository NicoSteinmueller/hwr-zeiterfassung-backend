package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.controller.TimeController;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.ProjectRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import com.hwr.hwrzeiterfassung.response.models.TimeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/book")
public class BookController {
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private LoginController loginController;
    @Autowired
    private TimeController timeController;

    @PostMapping(path = "/time")
    public ResponseEntity<HttpStatus> addTimeEntry(@RequestParam String email, @RequestParam String password,
                                                   @RequestParam boolean isStart, @RequestParam boolean pause, @RequestParam String note, @RequestParam int projectId) {

        loginController.validateLoginInformation(email, password);

        var datetime = LocalDateTime.now();

        var human = humanRepository.findById(email);
        if (human.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Human doesn't exists");
        }

        var project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Project with this id exists");
        }

        var dayList = dayRepository.findAllByDateAndHuman_Email(datetime.toLocalDate(), email);
        if (dayList.isEmpty()) {
            dayRepository.saveAndFlush(new Day(datetime.toLocalDate(), human.get().getTargetDailyWorkingTime(), human.get()));
            dayList = dayRepository.findAllByDateAndHuman_Email(datetime.toLocalDate(), email);
        }
        var day = dayList.get(dayList.size() - 1);


        var times = timeRepository.findAllByDayAndPause(day, pause);
        if (!times.isEmpty()) {
            var time = times.get(times.size() - 1);
            if (time.getEnd() == null) {
                if (isStart) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Endtime for the previous Booking on this day.");
                } else {
                    time.setEnd(datetime);
                    timeRepository.saveAndFlush(time);
                    double workTime = (timeController.calculateEntriesTimeInMinutes(timeRepository.findAllByDayAndPause(day, false)) / (double) 60) - day.getTargetDailyWorkingTime();
                    day.setWorkingTimeDifference(workTime);
                    day.setPauseTime(calculatePauseTime(timeRepository.findAllByDayAndPause(day, false), timeRepository.findAllByDayAndPause(day, true)));
                    correctPauseTime(day);
                    dayRepository.saveAndFlush(day);
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
            }
        }
        if (isStart) {
            timeRepository.saveAndFlush(new Time(datetime, null, pause, note, day, project.get()));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            //Tagesübergangsbuchung Regelung
            var lastDays = dayRepository.findAllByDateAndHuman_Email(datetime.minusDays(1).toLocalDate(), email);

            if (!lastDays.isEmpty()) {
                var lastTimes = timeRepository.findAllByDay(lastDays.get(0));
                if (!lastTimes.isEmpty()) {
                    var lastTime = lastTimes.get(lastDays.size() - 1);
                    if (lastTime.getEnd() == null) {
                        lastTime.setEnd(LocalDateTime.of(lastDays.get(0).getDate(), LocalTime.MAX));
                        timeRepository.saveAndFlush(lastTime);

                        timeRepository.saveAndFlush(new Time(LocalDateTime.of(day.getDate(), LocalTime.MIDNIGHT), datetime, pause, note, day, project.get()));
                        return new ResponseEntity<>(HttpStatus.ACCEPTED);
                    }
                }
            }

            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Starttime for the Booking.");
        }
    }


    public double calculatePauseTime(List<Time> workList, List<Time> pauseList) {
        double pause = 0;
        pause += timeController.calculateEntriesTimeInMinutes(pauseList);
        pause += timeController.calculateTimeBetweenEntriesInMinutes(workList);
        pause /= 60;
        return pause;
    }

    public void correctPauseTime(Day day) {
        double workTime = day.getTargetDailyWorkingTime();
        double pauseTime = day.getPauseTime();
        double minPause = 0;
        if (workTime >= 10) {
            minPause = Math.max(pauseTime, 1);
        }
        if (workTime >= 9) {
            minPause = Math.max(pauseTime, (double) 45 / 60);
        }
        if (workTime >= 6) {
            minPause = Math.max(pauseTime, (double) 30 / 60);
        }

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


    @GetMapping(path = "/lastTimeStatus")
    public @ResponseBody
    Optional<TimeAction> isTimeStatusToBookStart(@RequestParam String email, @RequestParam String password) {
        loginController.validateLoginInformation(email, password);


        var day = dayRepository.findAllByDateAndHuman_Email(LocalDate.now(), email);
        if (day.isEmpty())
            return Optional.empty();

        var times = timeRepository.findAllByDay(day.get(day.size() - 1));
        if (times.isEmpty()) {
            //TODO Sonderregel für Start am Vortag
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
            return Optional.of(new TimeAction(false, time.isPause(), time.getProject().getId()));
        return Optional.of(new TimeAction(true, time.isPause(), time.getProject().getId()));
    }
}
