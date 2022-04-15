package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * this Entity is for the single time data
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Time {
    /**
     * the id to* clearly identify one time in the DB
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    /**
     * the start-time for the time
     */
    @Column(columnDefinition = "datetime")
    private LocalDateTime start;

    /**
     * the end-time for the time
     */
    @Column(columnDefinition = "datetime")
    private LocalDateTime end;

    /**
     * if the time is a pause / break
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean pause;

    /**
     * a note for the time
     */
    @Column(columnDefinition = "TEXT")
    private String note;

    /**
     * links the time to a day, with a link to human
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Day day;

    /**
     * the project being worked on
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Project project;

    /**
     * constructor for the time Entity
     *
     * @param start   the start-time for the time
     * @param end     the end-time for the time
     * @param pause   if the time is a pause / break
     * @param note    a note for the time
     * @param day     links the time to a day, with a link to human
     * @param project the project being worked on
     */
    public Time(LocalDateTime start, LocalDateTime end, boolean pause, String note, Day day, Project project) {
        this.start = start;
        this.end = end;
        this.pause = pause;
        this.note = note;
        this.day = day;
        this.project = project;
    }
}
