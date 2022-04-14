package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class DayControllerTest {
    @Mock
    DayRepository dayRepository;
    @InjectMocks
    DayController dayController;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        LocalDate secondDate = LocalDate.of(2022, 4, 2);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Day day = new Day(date, 8.0, human);
        Day secondDay = new Day(secondDate, 8.0, human);
        List<Day> dayList = new ArrayList<>();
        dayList.add(day);
        List<Day> secondList = new ArrayList<>();
        secondList.add(secondDay);
        when(dayRepository.findAllByDateAndHuman_Email(date, "test")).thenReturn(dayList);
        when(dayRepository.findAllByDateAndHuman_Email(secondDate, "test")).thenReturn(new ArrayList<>()).thenReturn(secondList);
    }

    @Test
    void getDayByDateAndHuman() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");

        var result = dayController.getDayOrCreateDayByDateAndHuman(date, human);
        assertEquals(date, result.getDate());
        assertEquals("test", result.getHuman().getEmail());
    }

    @Test
    void createDayByDateAndHuman() {
        LocalDate date = LocalDate.of(2022, 4, 2);
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");

        var result = dayController.getDayOrCreateDayByDateAndHuman(date, human);
        assertEquals(date, result.getDate());
        assertEquals("test", result.getHuman().getEmail());
    }

}
