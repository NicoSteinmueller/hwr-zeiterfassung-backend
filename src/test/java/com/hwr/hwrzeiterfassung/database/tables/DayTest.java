package com.hwr.hwrzeiterfassung.database.tables;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DayTest {
    @Test
    void calculateWorkingTimeValid() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        human.setTargetDailyWorkingTime(8.0);
        Day day = new Day(date, human.getTargetDailyWorkingTime(), human);

        day.setWorkingTimeDifference(-1.0);

        var result = day.calculateWorkingTime();
        assertEquals(7.0, result);
    }

    @Test
    void calculateWorkingTimeNullTargetAndDifference() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, null, human);

        var result = day.calculateWorkingTime();
        assertEquals(0.0, result);
    }

    @Test
    void calculateWorkingTimeNullTarget() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, null, human);

        day.setWorkingTimeDifference(2.0);

        var result = day.calculateWorkingTime();
        assertEquals(2.0, result);
    }

    @Test
    void calculateWorkingTimeNullDifference() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);

        var result = day.calculateWorkingTime();
        assertEquals(8.0, result);
    }

    @Test
    void calculateWorkingTimeSub0() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        human.setTargetDailyWorkingTime(8.0);
        Day day = new Day(date, human.getTargetDailyWorkingTime(), human);

        day.setWorkingTimeDifference(-8.1);

        var result = day.calculateWorkingTime();
        //assertEquals(0.0, result);
    }

    @Test
    void calculateWorkingTimeOver24() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        human.setTargetDailyWorkingTime(12.0);
        Day day = new Day(date, human.getTargetDailyWorkingTime(), human);

        day.setWorkingTimeDifference(12.1);

        var result = day.calculateWorkingTime();
        //assertEquals(24.0, result);
    }

    @Test
    void getDayOverviewFullEmptyDay() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        human.setTargetDailyWorkingTime(8.0);
        Day day = new Day(date, human.getTargetDailyWorkingTime(), human);
        day.setTimes(new HashSet<>());

        var result = day.getDayOverviewFull();
        assertEquals(date, result.getDate());
        assertEquals(null, result.getStart());
    }

    @Test
    void getDayOverviewFullOneTime() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);

        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 12, 0);
        Time time = new Time(start, end, false, "", day, null);

        Set<Time> times = new HashSet<>();
        times.add(time);
        day.setTimes(times);

        var result = day.getDayOverviewFull();
        assertEquals(date, result.getDate());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
    }

    @Test
    void getDayOverviewFullTwoTimes() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);

        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime breakStart = LocalDateTime.of(2022, 4, 1, 12, 0);
        LocalDateTime breakEnd = LocalDateTime.of(2022, 4, 1, 12, 15);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 13, 0);
        Time time = new Time(start, breakStart, false, "", day, null);
        Time time2 = new Time(breakEnd, end, false, "", day, null);

        Set<Time> times = new HashSet<>();
        times.add(time);
        times.add(time2);
        day.setTimes(times);

        var result = day.getDayOverviewFull();
        assertEquals(date, result.getDate());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
    }

    @Test
    void getDayOverviewFullOverlappingTimes() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);

        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime breakStart = LocalDateTime.of(2022, 4, 1, 12, 0);
        LocalDateTime breakEnd = LocalDateTime.of(2022, 4, 1, 12, 15);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 13, 0);
        Time time = new Time(start, breakEnd, false, "", day, null);
        Time time2 = new Time(breakStart, end, false, "", day, null);

        Set<Time> times = new HashSet<>();
        times.add(time);
        times.add(time2);
        day.setTimes(times);

        var result = day.getDayOverviewFull();
        assertEquals(date, result.getDate());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
    }
}