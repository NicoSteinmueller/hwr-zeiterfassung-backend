package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.HumanController;
import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.controller.TimeController;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import com.hwr.hwrzeiterfassung.response.models.DateAndListOfTimes;
import com.hwr.hwrzeiterfassung.response.models.TimeCompact;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CorrectTimeControllerTest {
    @Mock
    LoginController loginController;
    @Mock
    DayRepository dayRepository;
    @Mock
    TimeRepository timeRepository;
    @Mock
    TimeController timeController;
    @Mock
    HumanController humanController;
    @InjectMocks
    CorrectTimeController correctTimeController;

    @Test
    void getDayInformationAndTimesNoDay() {
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(new ArrayList<>());

        var result = correctTimeController.getDayInformationAndTimes("test", "", LocalDate.now());
        assertThat(result).isEmpty();
    }

    @Test
    void getDayInformationAndTimesNoTimes() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        day.setWorkingTimeDifference(0.0);
        day.setTargetDailyWorkingTime(8.0);
        List<Day> days = new ArrayList<>();
        days.add(day);

        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDay(Mockito.any())).thenReturn(new ArrayList<>());

        var result = correctTimeController.getDayInformationAndTimes("", "", LocalDate.now());
        assertThat(result).isPresent();
        var resultDay = result.get().getDayOverviewCompact();
        assertEquals(LocalDate.now(), resultDay.getDate());
        assertNull(result.get().getTimes());
    }

    @Test
    void getDayInformationAndTimesFull() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        day.setWorkingTimeDifference(0.0);
        day.setTargetDailyWorkingTime(8.0);
        List<Day> days = new ArrayList<>();
        days.add(day);
        Time time = new Time();
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));
        time.setStart(start);
        time.setEnd(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time);

        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDay(Mockito.any())).thenReturn(times);

        var result = correctTimeController.getDayInformationAndTimes("", "", LocalDate.now());
        assertThat(result).isPresent();
        var resultDay = result.get().getDayOverviewCompact();
        var resultTimes = result.get().getTimes();
        assertEquals(LocalDate.now(), resultDay.getDate());
        assertEquals(start, resultTimes.get(0).getStart());
    }

    @Test
    void changeDayTimesNewDay() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 9, 0);
        Project project = new Project();
        project.setName("");
        TimeCompact timeCompact = new TimeCompact(start, end, false, "", project);
        List<TimeCompact> timesCompact = new ArrayList<>();
        timesCompact.add(timeCompact);
        Day day = new Day();
        day.setDate(date);
        day.setWorkingTimeDifference(0.0);
        day.setTargetDailyWorkingTime(8.0);
        Time time = new Time(start, end, false, "", day, project);
        List<Time> times = new ArrayList<>();
        times.add(time);
        DateAndListOfTimes input = new DateAndListOfTimes(date, timesCompact);

        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(new ArrayList<>());
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);
        when(humanController.getHumanByEmail(Mockito.any())).thenReturn(new Human());

        var result = correctTimeController.changeDayTimes("", "", input);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    void changeDayTimesDayExists() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 9, 0);
        Project project = new Project();
        project.setName("");
        TimeCompact timeCompact = new TimeCompact(start, end, false, "", project);
        List<TimeCompact> timesCompact = new ArrayList<>();
        timesCompact.add(timeCompact);
        Day day = new Day();
        day.setDate(date);
        List<Day> days = new ArrayList<>();
        days.add(day);
        Time time = new Time(start, end, false, "", day, project);
        List<Time> times = new ArrayList<>();
        times.add(time);
        DateAndListOfTimes input = new DateAndListOfTimes(date, timesCompact);

        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);

        var result = correctTimeController.changeDayTimes("", "", input);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }
}