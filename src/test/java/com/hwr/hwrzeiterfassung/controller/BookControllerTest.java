package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.*;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Mock
    LoginController loginController;
    @Mock
    HumanController humanController;
    @Mock
    ProjectController projectController;
    @Mock
    DayController dayController;
    @Mock
    TimeRepository timeRepository;
    @Mock
    TimeController timeController;
    @Mock
    DayRepository dayRepository;
    @InjectMocks
    BookController bookController;

    @BeforeEach
    void setUp() {
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        when(humanController.getHumanByEmail(Mockito.anyString())).thenReturn(human);
        Project project = new Project();
        project.setName("Test");
        when(projectController.getProjectById(Mockito.anyInt())).thenReturn(project);
    }

    @Test
    void addTimeEntryTimeWithoutEndAndIsStart() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        Time time = new Time();
        time.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time);
        List<Day> days = new ArrayList<>();
        days.add(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(Mockito.any(), Mockito.any())).thenReturn(day);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);
        var e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry("test", "test", true, false, "", 1));
        assertEquals("No Endtime for the previous Booking on this day.", e.getReason());
    }

    @Test
    void addTimeEntryWithoutEndAndIsNotStart() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        Time time = new Time();
        time.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time);
        List<Day> days = new ArrayList<>();
        days.add(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(Mockito.any(), Mockito.any())).thenReturn(day);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);
        var result = bookController.addTimeEntry("test", "test", false, false, "", 1);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    void addTimeEntryNoTimeAndIsStart() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        List<Time> times = new ArrayList<>();
        List<Day> days = new ArrayList<>();
        days.add(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(Mockito.any(), Mockito.any())).thenReturn(day);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);
        var result = bookController.addTimeEntry("test", "test", true, false, "", 1);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    void addTimeEntryTimeEndAndIsStart() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        Time time = new Time();
        time.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        time.setEnd(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time);
        List<Day> days = new ArrayList<>();
        days.add(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(Mockito.any(), Mockito.any())).thenReturn(day);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);
        var result = bookController.addTimeEntry("test", "test", true, false, "", 1);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    void addTimeEntryNoEndYesterday() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        Day yesterday = new Day();
        day.setDate(LocalDate.now().minusDays(1));
        Time time = new Time();
        time.setStart(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(8, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time);
        List<Day> days = new ArrayList<>();
        days.add(yesterday);
        when(dayController.getDayOrCreateDayByDateAndHuman(eq(LocalDate.now()), Mockito.any())).thenReturn(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(eq(LocalDate.now().minusDays(1)), Mockito.any())).thenReturn(yesterday);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(eq(day), Mockito.anyBoolean())).thenReturn(new ArrayList<>());
        when(timeRepository.findAllByDayAndPause(eq(yesterday), Mockito.anyBoolean())).thenReturn(times);
        var result = bookController.addTimeEntry("test", "test", false, false, "", 1);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    void addTimeEntryNoTimeYesterday() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        Day yesterday = new Day();
        day.setDate(LocalDate.now().minusDays(1));
        List<Time> times = new ArrayList<>();
        List<Day> days = new ArrayList<>();
        days.add(yesterday);
        when(dayController.getDayOrCreateDayByDateAndHuman(eq(LocalDate.now()), Mockito.any())).thenReturn(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(eq(LocalDate.now().minusDays(1)), Mockito.any())).thenReturn(yesterday);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(eq(day), Mockito.anyBoolean())).thenReturn(times);
        when(timeRepository.findAllByDayAndPause(eq(yesterday), Mockito.anyBoolean())).thenReturn(times);

        var e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry("test", "test", false, false, "", 1));
        assertEquals("No Starttime for the Booking.", e.getReason());
    }

    @Test
    void addTimeEntryNoUnfinishedTimeYesterday() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        Day yesterday = new Day();
        day.setDate(LocalDate.now().minusDays(1));
        Time time = new Time();
        time.setStart(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(8, 0)));
        time.setEnd(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(12, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time);
        List<Day> days = new ArrayList<>();
        days.add(yesterday);
        when(dayController.getDayOrCreateDayByDateAndHuman(eq(LocalDate.now()), Mockito.any())).thenReturn(day);
        when(dayController.getDayOrCreateDayByDateAndHuman(eq(LocalDate.now().minusDays(1)), Mockito.any())).thenReturn(yesterday);
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(eq(day), Mockito.anyBoolean())).thenReturn(new ArrayList<>());
        when(timeRepository.findAllByDayAndPause(eq(yesterday), Mockito.anyBoolean())).thenReturn(times);

        var e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry("test", "test", false, false, "", 1));
        assertEquals("No Starttime for the Booking.", e.getReason());
    }

    @Test
    void getLastTimeFromNonexistentDay() {
        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(new ArrayList<>());

        var result = bookController.getLastTimeFromDay(LocalDate.now(), "test", false);
        assertThat(result).isEmpty();
    }

    @Test
    void getLastTimeFromEmptyDay() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        List<Day> days = new ArrayList<>();
        days.add(day);

        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(new ArrayList<>());

        var result = bookController.getLastTimeFromDay(LocalDate.now(), "test", false);
        assertThat(result).isEmpty();
    }

    @Test
    void getLastTimeFromDay() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        List<Day> days = new ArrayList<>();
        days.add(day);
        Time time1 = new Time();
        time1.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        time1.setEnd(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0)));
        Time time2 = new Time();
        time2.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        time2.setEnd(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)));
        List<Time> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);

        when(dayRepository.findAllByDateAndHuman_Email(Mockito.any(), Mockito.anyString())).thenReturn(days);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(times);

        var result = bookController.getLastTimeFromDay(LocalDate.now(), "test", false);
        assertThat(result).isPresent();
        assertEquals(time2, result.get());
    }
}
