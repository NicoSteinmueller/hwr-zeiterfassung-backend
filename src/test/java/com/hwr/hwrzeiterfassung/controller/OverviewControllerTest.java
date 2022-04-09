package com.hwr.hwrzeiterfassung.controller;

import com.google.common.hash.Hashing;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewCompact;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewFull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class OverviewControllerTest {
    @Autowired
    private OverviewController overviewController;

    @Test
    void getDayFullOverviewsInInterval() {
        //Todo interval with one day
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        LocalDate start = LocalDate.of(2022, 3, 1);
        LocalDate end = LocalDate.of(2022, 4, 1);
        Iterable<DayOverviewFull> overview = overviewController.getDayFullOverviewsInInterval(email, password, start, end);
        DayOverviewFull singleDay = overview.iterator().next();

        LocalDateTime startTime = LocalDateTime.of(2022, 3, 29, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 3, 29, 17, 0);
        DayOverviewFull expected = new DayOverviewFull(start, startTime, endTime, 1.0, 8.0);

        assertEquals(expected, singleDay);
    }

    @Test
    void getDayCompactOverviewsInInterval() {
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        LocalDate start = LocalDate.of(2022, 3, 1);
        LocalDate end = LocalDate.of(2022, 4, 1);
        Iterable<DayOverviewCompact> overview = overviewController.getDayCompactOverviewsInInterval(email, password, start, end);
        DayOverviewCompact singleDay = overview.iterator().next();
        DayOverviewCompact expected = new DayOverviewCompact(start, 1.0, 8.0);

        assertEquals(expected, singleDay);
    }

    @Test
    void getAverageWorkingHoursInInterval() {
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        LocalDate start = LocalDate.of(2022, 3, 1);
        LocalDate end = LocalDate.of(2022, 4, 1);
        double avgWorkingHrs = overviewController.getAverageWorkingHoursInInterval(email, password, start, end);
        assertEquals(8.0, avgWorkingHrs);
    }

    @Test
    void getAveragePauseInInterval() {
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        LocalDate start = LocalDate.of(2022, 3, 1);
        LocalDate end = LocalDate.of(2022, 4, 1);
        double avgPause = overviewController.getAveragePauseInInterval(email, password, start, end);
        assertEquals(1.0, avgPause);
    }
}