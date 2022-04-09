package com.hwr.hwrzeiterfassung.controller;

import com.google.common.hash.Hashing;
import com.hwr.hwrzeiterfassung.database.repositorys.DayRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.TimeRepository;
import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import com.hwr.hwrzeiterfassung.response.models.TimeAction;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("UnstableApiUsage")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {
    @Autowired
    private BookController bookController;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private DayRepository dayRepository;

    @Test
    @Order(1)
    void addTimeEntryNoStartTime(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        boolean isStart = false;
        boolean pause = false;
        String note = "";
        int projectId = 1;
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry(email, password, isStart, pause, note, projectId));
        assertEquals("No Starttime for the Booking.", e.getReason());
    }

    @Test
    @Order(2)
    void addTimeEntryValidFirst() {
        //TODO: valid test data
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        boolean isStart = true;
        boolean pause = false;
        String note = "";
        int projectId = 1;
        var result = bookController.addTimeEntry(email, password, isStart, pause, note, projectId);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    @Order(3)
    void addTimeEntryNoEndTime(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        boolean isStart = true;
        boolean pause = false;
        String note = "";
        int projectId = 1;
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry(email, password, isStart, pause, note, projectId));
        assertEquals("No Endtime for the previous Booking on this day.", e.getReason());
    }

    @Test
    @Order(4)
    void addTimeEntryValidEndTime(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        boolean isStart = false;
        boolean pause = false;
        String note = "";
        int projectId = 1;
        var result = bookController.addTimeEntry(email, password, isStart, pause, note, projectId);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
    }

    @Test
    @Order(6)
    void addTimeEntryNoUser(){
        //Todo: user in login without human
        String email = "wrongUser";
        String password = "mockPassword";
        boolean isStart = false;
        boolean pause = false;
        String note = "";
        int projectId = 1;
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry(email, password, isStart, pause, note, projectId));
        assertEquals("Human doesn't exists", e.getReason());
    }

    @Test
    @Order(7)
    void addTimeEntryNoProject(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        boolean isStart = false;
        boolean pause = false;
        String note = "";
        int projectId = -51;
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> bookController.addTimeEntry(email, password, isStart, pause, note, projectId));
        assertEquals("No Project with this id exists", e.getReason());
    }

    @Test
    @Order(8)
    void calculatePauseTimeTest(){
        LocalDate today = LocalDateTime.now().toLocalDate(); //replace with test data
        Day testDay = dayRepository.findAllByDateAndHuman_Email(today, "test").get(0);
        List<Time> worklist = timeRepository.findAllByDayAndPause(testDay, false);
        List<Time> pauseList = timeRepository.findAllByDayAndPause(testDay, true);
        double pauseTime = bookController.calculatePauseTime(worklist, pauseList);
        assertEquals(0.0, pauseTime);
    }

    @Test
    @Order(9)
    void correctPauseTimeTest(){
        //Todo: Testdata für verschiedene Case (worktime >= 10, 10 > worktime >= 9 etc. sowie minPause <,=,> pauseTime), dann Test copy-pasten
        LocalDate today = LocalDateTime.now().toLocalDate();
        Day testDay = dayRepository.findAllByDateAndHuman_Email(today, "test").get(0);
        bookController.correctPauseTime(testDay);
        assertTrue(true);
    }

    @Test
    @Order(10)
    void isTimeStatusToBookStartEmptyDay(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        TimeAction status = bookController.isTimeStatusToBookStart(email, password);
        assertEquals(null, status);
    }

    @Test
    @Order(11)
    void isTimeStatusToBookStartEmptyTime(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        TimeAction status = bookController.isTimeStatusToBookStart(email, password);
        assertEquals(null, status);
    }

    @Test
    @Order(12)
    void isTimeStatusToBookStartFalse(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        TimeAction status = bookController.isTimeStatusToBookStart(email, password);
        assertEquals(new TimeAction(false, false, 1), status);
    }

    @Test
    @Order(13)
    void isTimeStatusToBookStartTrue(){
        String email = "test";
        String password = Hashing.sha256().hashString("", StandardCharsets.UTF_8).toString();
        TimeAction status = bookController.isTimeStatusToBookStart(email, password);
        assertEquals(new TimeAction(true, false, 1), status);
    }
}