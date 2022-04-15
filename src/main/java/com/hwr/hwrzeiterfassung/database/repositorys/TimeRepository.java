package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Day;
import com.hwr.hwrzeiterfassung.database.tables.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface to access all times
 */
public interface TimeRepository extends JpaRepository<Time, Integer> {
    /**
     * find all times that are associated with a day and divided by pause
     *
     * @param day   the day of the times
     * @param pause if the times are a pause
     * @return List of Time
     */
    List<Time> findAllByDayAndPause(Day day, boolean pause);

    /**
     * find all times of a day
     *
     * @param day the day of the times
     * @return List of Time
     */
    List<Time> findAllByDay(Day day);

    /**
     * delete all times from a day
     *
     * @param day the day of the times
     */
    void deleteAllByDay(Day day);
}
