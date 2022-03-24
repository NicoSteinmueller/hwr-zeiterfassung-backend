package com.hwr.hwrzeiterfassung.database.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "human")
public class Human {
    @Id
    //TODO Mail Validierung mit 64zeichen'@'255zeichen = 320
    @Column(name = "email", columnDefinition = "VARCHAR(320)")
    private String email;

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(256)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(256)")
    private String lastName;

    @Column(name = "target_daily_working_time", columnDefinition = "DOUBLE")
    private double targetDailyWorkingTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supervisor_email")
    private Human supervisor;

    @OneToMany(mappedBy = "supervisor")
    private Set<Human> subordinate;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_name", referencedColumnName = "name")
    private Role role;

    @OneToOne(mappedBy = "human", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private Login login;


    @OneToMany(mappedBy = "human")
    @JsonIgnore
    private Set<Day> days;

    @ManyToMany
    @JoinTable(name = "project_access")
    private Set<Project> projects;
}
