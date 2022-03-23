package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Data
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "start", columnDefinition = "datetime")
    private DateTime start;

    @Column(name = "end", columnDefinition = "datetime")
    private DateTime end;

    @Column(name = "isBreak", nullable = false, columnDefinition = "bit default 0")
    private boolean isBreak;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "day_id")
    private Day day;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;
}
