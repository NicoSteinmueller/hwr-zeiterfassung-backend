package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Long> {
    List<Day> findAllByDateAndHuman_Email(LocalDate date, String email);

    List<Day> findAllByDateBetweenAndHuman_Email(LocalDate start, LocalDate end, String human_email);

    @Query("SELECT AVG(day.targetDailyWorkingTime + day.workingTimeDifference) from Day day where day.human.email = :mail and day.date between :start and :end")
    Optional<Double> getWorkingTimeAverageByDateBetweenAndHuman_Email(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("mail") String human_email);

    @Query("SELECT AVG(day.pauseTime) from Day day where day.human.email = :mail and day.date between :start and :end")
    Optional<Double> getPauseAverageByDateBetweenAndHuman_Email(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("mail") String human_email);
}
