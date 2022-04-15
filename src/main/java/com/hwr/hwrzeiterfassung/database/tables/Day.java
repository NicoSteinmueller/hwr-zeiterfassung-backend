package com.hwr.hwrzeiterfassung.database.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hwr.hwrzeiterfassung.response.models.DayOverviewFull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * this Entity is for the single day data
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Day {
    /**
     * the id for clearly identify one day in the DB
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    /**
     * the time that is scheduled to be worked on the day
     */
    @Column(columnDefinition = "DOUBLE")
    private Double targetDailyWorkingTime;

    /**
     * the date of the day
     */
    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate date;

    /**
     * how long a pause was taken during the day
     */
    @Column(columnDefinition = "DOUBLE")
    private Double pauseTime;

    /**
     * the difference between the target daily working time and the real working time of the day
     */
    @Column(columnDefinition = "DOUBLE")
    private Double workingTimeDifference;

    /**
     * the link to the human
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Human human;

    /**
     * times from the day
     */
    @OneToMany(mappedBy = "day")
    @JsonIgnore
    private Set<Time> times;

    /**
     * constructor for the Day
     *
     * @param date                   the date for the day
     * @param targetDailyWorkingTime the time the human has to work on this day
     * @param human                  the human associated with the tag
     */
    public Day(LocalDate date, Double targetDailyWorkingTime, Human human) {
        this.date = date;
        this.targetDailyWorkingTime = targetDailyWorkingTime;
        this.human = human;
    }

    /**
     * calculates the time worked on this day
     *
     * @return the working time for the day
     */
    public double calculateWorkingTime() {
        double workingTime = 0;
        if (Objects.isNull(targetDailyWorkingTime))
            workingTime += 0;
        else
            workingTime += targetDailyWorkingTime;
        if (Objects.isNull(workingTimeDifference))
            workingTime += 0;
        else
            workingTime += workingTimeDifference;

        return workingTime;
    }

    /**
     * create a Full Overview from this day
     *
     * @return Day Overview Full
     */
    public DayOverviewFull getDayOverviewFull() {
        if (times.isEmpty())
            return new DayOverviewFull(date, null, null, pauseTime, calculateWorkingTime());

        var dayStart = LocalDateTime.MAX;
        var dayEnd = LocalDateTime.MIN;

        for (Time time : times) {
            if (time.getStart() != null && dayStart.isAfter(time.getStart()))
                dayStart = time.getStart();
            if (time.getEnd() != null && dayEnd.isBefore(time.getEnd()))
                dayEnd = time.getEnd();
        }
        if (dayStart == LocalDateTime.MAX)
            dayStart = null;
        if (dayEnd == LocalDateTime.MIN)
            dayEnd = null;

        return new DayOverviewFull(date, dayStart, dayEnd, pauseTime, calculateWorkingTime());
    }

}
