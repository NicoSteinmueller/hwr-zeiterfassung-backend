package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {
    List<Day> findAllByDateAndHuman_Email(LocalDate date, String email);
}
