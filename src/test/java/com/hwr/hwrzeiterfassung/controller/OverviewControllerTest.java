package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewCompact;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewFull;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class OverviewControllerTest {
    @Mock
    LoginController loginController;
    @Mock
    DayRepository dayRepository;
    @InjectMocks
    OverviewController overviewController;

    @Test
    void getDayFullOverviewsInIntervalEmpty() {
        when(dayRepository.findAllByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(new ArrayList<>());

        var e = assertThrows(ResponseStatusException.class, () -> overviewController.getDayFullOverviewsInInterval("", "", LocalDate.now(), LocalDate.now()));
        assertEquals("No days found for the user in the interval", e.getReason());
    }

    @Test
    void getDayFullOverviewsInIntervalContent() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        day.setPauseTime(0.0);
        day.setTargetDailyWorkingTime(8.0);
        day.setWorkingTimeDifference(0.0);
        day.setTimes(new HashSet<>());
        List<Day> days = new ArrayList<>();
        days.add(day);
        when(dayRepository.findAllByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(days);

        var result = (List<DayOverviewFull>) overviewController.getDayFullOverviewsInInterval("", "", LocalDate.now(), LocalDate.now());
        assertThat(result).hasSize(1);
        assertEquals(LocalDate.now(), result.get(0).getDate());
    }

    @Test
    void getDayCompactOverviewsInIntervalEmpty() {
        when(dayRepository.findAllByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(new ArrayList<>());

        var e = assertThrows(ResponseStatusException.class, () -> overviewController.getDayCompactOverviewsInInterval("", "", LocalDate.now(), LocalDate.now()));
        assertEquals("No days found for the user in the interval", e.getReason());
    }

    @Test
    void getDayCompactOverviewsInIntervalContent() {
        Day day = new Day();
        day.setDate(LocalDate.now());
        day.setPauseTime(0.0);
        day.setTargetDailyWorkingTime(8.0);
        day.setWorkingTimeDifference(0.0);
        List<Day> days = new ArrayList<>();
        days.add(day);
        when(dayRepository.findAllByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(days);

        var result = (List<DayOverviewCompact>) overviewController.getDayCompactOverviewsInInterval("", "", LocalDate.now(), LocalDate.now());
        assertThat(result).hasSize(1);
        assertEquals(LocalDate.now(), result.get(0).getDate());
    }

    @Test
    void getAverageWorkingHoursInIntervalEmpty() {
        var today = LocalDate.now();
        when(dayRepository.getWorkingTimeAverageByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        var result = overviewController.getAverageWorkingHoursInInterval("", "", today, today);
        assertEquals(0.0, result);
    }

    @Test
    void getAverageWorkingHoursInIntervalContent() {
        var today = LocalDate.now();
        when(dayRepository.getWorkingTimeAverageByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.of(8.0));
        var result = overviewController.getAverageWorkingHoursInInterval("", "", today, today);
        assertEquals(8.0, result);
    }

    @Test
    void getAveragePauseInIntervalEmpty() {
        var today = LocalDate.now();
        when(dayRepository.getPauseAverageByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        var result = overviewController.getAveragePauseInInterval("", "", today, today);
        assertEquals(0.0, result);
    }

    @Test
    void getAveragePauseInIntervalContent() {
        var today = LocalDate.now();
        when(dayRepository.getPauseAverageByDateBetweenAndHuman_Email(Mockito.any(), Mockito.any(), Mockito.anyString()))
                .thenReturn(Optional.of(1.0));
        var result = overviewController.getAveragePauseInInterval("", "", today, today);
        assertEquals(1.0, result);
    }
}