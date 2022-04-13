package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestEntityManager
class TimeRepositoryTest {
    @Autowired
    TimeRepository timeRepository;

    @Test
    void findAllByDayAndPause() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human testHuman = new Human();
        testHuman.setEmail("test");
        testHuman.setFirstName("Tester");
        testHuman.setLastName(("von Test"));
        Day testDay = new Day(date, 8.0, testHuman);
        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 12, 0);
        Project project = new Project();
        project.setName("testProject");
        Time time = new Time(start, end, false, "", testDay, project);
        LocalDateTime pauseEnd = LocalDateTime.of(2022, 4, 1, 12, 30);
        Time pause = new Time(end, pauseEnd, true, "", testDay, project);
        timeRepository.saveAndFlush(time);
        timeRepository.saveAndFlush(pause);

        var test = timeRepository.findAllByDayAndPause(testDay, false);
        assertThat(test).hasSize(1);
        assertEquals(time, test.get(0));
    }

    @Test
    void findAllByDay() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        Human testHuman = new Human();
        testHuman.setEmail("test");
        testHuman.setFirstName("Tester");
        testHuman.setLastName(("von Test"));
        Day testDay = new Day(date, 8.0, testHuman);
        LocalDateTime start = LocalDateTime.of(2022, 4, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2022, 4, 1, 12, 0);
        Project project = new Project();
        project.setName("testProject");
        Time time = new Time(start, end, false, "", testDay, project);
        LocalDateTime pauseEnd = LocalDateTime.of(2022, 4, 1, 12, 30);
        Time pause = new Time(end, pauseEnd, true, "", testDay, project);
        timeRepository.saveAndFlush(time);
        timeRepository.saveAndFlush(pause);

        var test = timeRepository.findAllByDay(testDay);
        assertThat(test).hasSize(2);
        assertEquals(pause, test.get(1));
    }
}
