package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.ProjectRepository;
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

import java.time.LocalDateTime;

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

    @PostMapping("/time")
    public ResponseEntity<HttpStatus> addTimeEntry(@RequestParam String email, @RequestParam String password,
                                                   @RequestParam boolean isStart, @RequestParam boolean pause, @RequestParam String note, @RequestParam int projectId) {

        if (!loginController.validateLoginInformation(email, password))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");

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

        //TODO Sonderregel für Start am Vortag hinzufügen
        var times = timeRepository.findAllByDayAndPause(day, pause);
        if (!times.isEmpty()) {
            var time = times.get(times.size() - 1);
            if (time.getEnd() == null) {
                if (isStart) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Endtime for the previous Booking on this day.");
                } else {
                    time.setEnd(datetime);
                    timeRepository.saveAndFlush(time);
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
            }
        }
        if (isStart) {
            timeRepository.saveAndFlush(new Time(datetime, null, pause, note, day, project.get()));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Starttime for the Booking.");
        }
    }
}
