package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface to access all days
 */
public interface DayRepository extends JpaRepository<Day, Long> {
    /**
     * find days (usually only one) form a human with a date
     *
     * @param date  the date of the day
     * @param email the email(id) from the human
     * @return List of day
     */
    List<Day> findAllByDateAndHuman_Email(LocalDate date, String email);

    /**
     * find all days from a human between two dates
     *
     * @param start       the start date for the search
     * @param end         the end date for the search
     * @param human_email the email from the human
     * @return List of day
     */
    List<Day> findAllByDateBetweenAndHuman_Email(LocalDate start, LocalDate end, String human_email);

    /**
     * get the average Working Time from a Human between two dates
     *
     * @param start       the start date for the search
     * @param end         the end date for the search
     * @param human_email the email from the human
     * @return Optional Double average Working Time
     */
    @Query("select avg(day.targetDailyWorkingTime + day.workingTimeDifference) from Day day where day.human.email = :mail and day.date between :start and :end")
    Optional<Double> getWorkingTimeAverageByDateBetweenAndHuman_Email(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("mail") String human_email);

    /**
     * get the average Pause Time from a Human between two dates
     *
     * @param start       the start date for the search
     * @param end         the end date for the search
     * @param human_email the email from the human
     * @return Optional Double average Pause Time
     */
    @Query("select avg (day.pauseTime) from Day day where day.human.email = :mail and day.date between :start and :end")
    Optional<Double> getPauseAverageByDateBetweenAndHuman_Email(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("mail") String human_email);
}
