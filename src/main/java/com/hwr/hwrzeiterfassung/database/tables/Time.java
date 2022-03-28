package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "start", columnDefinition = "datetime")
    private LocalDateTime start;

    @Column(name = "end", columnDefinition = "datetime")
    private LocalDateTime end;

    @Column(name = "pause", nullable = false, columnDefinition = "bit default 0")
    private boolean pause;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "day_id")
    private Day day;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    public Time(LocalDateTime start, LocalDateTime end, boolean pause, String note, Day day, Project project) {
        this.start = start;
        this.end = end;
        this.pause = pause;
        this.note = note;
        this.day = day;
        this.project = project;
    }
}
