package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

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
}
