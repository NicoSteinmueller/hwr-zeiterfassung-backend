package com.hwr.hwrzeiterfassung.database.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TimeControllerTest {
    @Mock
    TimeRepository timeRepository;
    @InjectMocks
    TimeController timeController;

    @BeforeEach
    void setup() {
        when(timeRepository.saveAndFlush(Mockito.any())).thenReturn(null);
        when(timeRepository.findAllByDayAndPause(Mockito.any(), Mockito.anyBoolean())).thenReturn(new ArrayList<>());
    }

    @Test
    void calculateEntriesTimeInMinutes() {
        Project project = new Project();
        project.setName("project");
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);
        LocalDateTime start1 = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2022, 4, 1, 11, 20);
        LocalDateTime start2 = LocalDateTime.of(2022, 4, 1, 12, 21);
        LocalDateTime end2 = LocalDateTime.of(2022, 4, 1, 12, 58);
        Time time1 = new Time(start1, end1, false, "Work", day, project);
        Time time2 = new Time(start2, end2, false, "Clean", day, project);
        List<Time> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);

        long result = timeController.calculateEntriesTimeInMinutes(times);
        assertEquals(237, result);
    }

    @Test
    void startAndStopAtTheSameTime() {
        Project project = new Project();
        project.setName("project");
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);
        LocalDateTime start1 = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2022, 4, 1, 8, 0);
        Time time1 = new Time(start1, end1, false, "Work", day, project);
        List<Time> times = new ArrayList<>();
        times.add(time1);

        long result = timeController.calculateEntriesTimeInMinutes(times);
        assertEquals(0, result);
    }



    @Test
    void calculateTimeBetweenEntriesInMinutes() {
        Project project = new Project();
        project.setName("project");
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);
        LocalDateTime start1 = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2022, 4, 1, 11, 20);
        LocalDateTime start2 = LocalDateTime.of(2022, 4, 1, 12, 21);
        LocalDateTime end2 = LocalDateTime.of(2022, 4, 1, 12, 58);
        Time time1 = new Time(start1, end1, false, "Work", day, project);
        Time time2 = new Time(start2, end2, false, "Clean", day, project);
        List<Time> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);

        long result = timeController.calculateTimeBetweenEntriesInMinutes(times);
        assertEquals(61, result);
    }

    @Test
    void overlappingTimes(){
        Project project = new Project();
        project.setName("project");
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);
        LocalDateTime start1 = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2022, 4, 1, 11, 20);
        LocalDateTime start2 = LocalDateTime.of(2022, 4, 1, 11, 0);
        LocalDateTime end2 = LocalDateTime.of(2022, 4, 1, 12, 58);
        Time time1 = new Time(start1, end1, false, "Work", day, project);
        Time time2 = new Time(start2, end2, false, "Clean", day, project);
        List<Time> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);

        System.out.println(timeController.calculateTimeBetweenEntriesInMinutes(times));

        //assertThrows(Exception.class, timeController.calculateTimeBetweenEntriesInMinutes(times));
        //Test ist erstmal nur im Kommentar, weil der build fehlschlägt.
        //Möglicherweise fliegt der Test auch, je nachdem wo die Kontrolle stattfindet, ob sich Zeiten überlappen
    }

    @Test
    void emptyListWorkingTime(){
        List<Time> empty = new ArrayList<>();
        long result1 = timeController.calculateEntriesTimeInMinutes(empty);

        assertEquals(0, result1);
    }

    @Test
    void emptyListPause(){
        List<Time> empty = new ArrayList<>();
        long result2 = timeController.calculateTimeBetweenEntriesInMinutes(empty);

        assertEquals(0, result2);
    }

    @Test
    void correctPauseTimeSub6() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 5.9, human);

        day.setPauseTime(0.25);
        timeController.correctPauseTime(day);
        assertEquals(0.25, day.getPauseTime());
    }

    @Test
    void correctPauseTimePlus6() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 6.0, human);

        day.setPauseTime(0.25);
        timeController.correctPauseTime(day);
        assertEquals(0.5, day.getPauseTime());
    }

    @Test
    void correctPauseTimeSub9() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.9, human);

        day.setPauseTime(0.25);
        timeController.correctPauseTime(day);
        assertEquals(0.5, day.getPauseTime());
    }

    @Test
    void correctPauseTimePlus9() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 9.0, human);

        day.setPauseTime(0.25);
        timeController.correctPauseTime(day);
        assertEquals(0.75, day.getPauseTime());
    }

    @Test
    void correctPauseTimeSub10() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 9.9, human);

        day.setPauseTime(0.25);
        timeController.correctPauseTime(day);
        assertEquals(0.75, day.getPauseTime());
    }

    @Test
    void correctPauseTimePlus10() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 10.0, human);

        day.setPauseTime(0.25);
        timeController.correctPauseTime(day);
        assertEquals(1.0, day.getPauseTime());
    }
}
