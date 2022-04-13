package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestEntityManager
class DayRepositoryTest {
    @Autowired
    DayRepository dayRepository;

    @Test
    void findAllByDateAndHuman_Email() {
        LocalDate date = LocalDate.of(2022, 4, 1);
        String email = "test";
        Human testHuman = new Human();
        testHuman.setEmail(email);
        testHuman.setFirstName("Test");
        testHuman.setLastName("Tester");
        Day testDay = new Day(date, 8.0, testHuman);
        dayRepository.saveAndFlush(testDay);

        var test = dayRepository.findAllByDateAndHuman_Email(date, email);
        assertThat(test).hasSize(1);
        assertEquals(testDay, test.get(0));
    }

    @Test
    void findAllByDateBetweenAndHuman_Email() {
        LocalDate date1 = LocalDate.of(2022, 4, 1);
        LocalDate date2 = LocalDate.of(2022, 4, 2);
        String email = "test";
        Human testHuman = new Human();
        testHuman.setEmail(email);
        testHuman.setFirstName("Test");
        testHuman.setLastName("Tester");
        Day testDay1 = new Day(date1, 8.0, testHuman);
        Day testDay2 = new Day(date2, 8.0, testHuman);
        dayRepository.saveAndFlush(testDay1);
        dayRepository.saveAndFlush(testDay2);

        var test = dayRepository.findAllByDateBetweenAndHuman_Email(date1, date2, email);
        assertThat(test).hasSize(2);
        assertEquals(testDay2, test.get(1));
    }

    @Test
    void getWorkingTimeAverageByDateBetweenAndHuman_Email() {
        LocalDate date1 = LocalDate.of(2022, 4, 1);
        LocalDate date2 = LocalDate.of(2022, 4, 2);
        String email = "test";
        Human testHuman = new Human();
        testHuman.setEmail(email);
        testHuman.setFirstName("Test");
        testHuman.setLastName("Tester");
        Day testDay1 = new Day(date1, 8.0, testHuman);
        Day testDay2 = new Day(date2, 8.0, testHuman);
        testDay1.setWorkingTimeDifference(-0.5);
        testDay2.setWorkingTimeDifference(1.0);
        dayRepository.saveAndFlush(testDay1);
        dayRepository.saveAndFlush(testDay2);

        var test = dayRepository.getWorkingTimeAverageByDateBetweenAndHuman_Email(date1, date2, email);
        assertThat(test).isNotEmpty();
        assertThat(test).hasValue(8.25);
    }

    @Test
    void getPauseAverageByDateBetweenAndHuman_Email() {
        LocalDate date1 = LocalDate.of(2022, 4, 1);
        LocalDate date2 = LocalDate.of(2022, 4, 2);
        String email = "test";
        Human testHuman = new Human();
        testHuman.setEmail(email);
        testHuman.setFirstName("Test");
        testHuman.setLastName("Tester");
        Day testDay1 = new Day(date1, 8.0, testHuman);
        Day testDay2 = new Day(date2, 8.0, testHuman);
        testDay1.setPauseTime(2.0);
        testDay2.setPauseTime(1.0);
        dayRepository.saveAndFlush(testDay1);
        dayRepository.saveAndFlush(testDay2);

        var test = dayRepository.getPauseAverageByDateBetweenAndHuman_Email(date1, date2, email);
        assertThat(test).isNotEmpty();
        assertThat(test).hasValue(1.5);
    }
}
