package com.hwr.hwrzeiterfassung.database.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "target_daily_working_time", columnDefinition = "DOUBLE")
    private double targetDailyWorkingTime;
    @Column(name = "day", nullable = false, columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "pause_time", columnDefinition = "DOUBLE")
    private double pauseTime;
    @Column(name = "working_time_difference", columnDefinition = "DOUBLE")
    private double workingTimeDifference;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Human human;

    @OneToMany(mappedBy = "day")
    @JsonIgnore
    private Set<Time> time;

    public Day(LocalDate date, double targetDailyWorkingTime, Human human) {
        this.date = date;
        this.targetDailyWorkingTime = targetDailyWorkingTime;
        this.human = human;
    }
}
