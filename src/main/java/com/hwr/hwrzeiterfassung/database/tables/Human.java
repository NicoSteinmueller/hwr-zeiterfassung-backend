package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * this Entity is for save the human data
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Human {
    /**
     * the id for clearly identifying a human in the DB and the email of the human
     */
    @Id
    //TODO Mail Validierung mit 64zeichen'@'255zeichen = 320
    @Column(columnDefinition = "VARCHAR(320)")
    private String email;

    /**
     * the fist name of the human
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(256)")
    private String firstName;

    /**
     * the second name of the human
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(256)")
    private String lastName;

    /**
     * the target daily working time of the human
     */
    @Column(columnDefinition = "DOUBLE")
    private Double targetDailyWorkingTime;

    /**
     * the supervisor of the human, which is also a human
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Human supervisor;

    /**
     * the project that the human uses by default
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Project defaultProject;

    /**
     * the role that the human has
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Role role;

    /**
     * the projects that humans have access to
     */
    @ManyToMany
    @JoinTable
    private Set<Project> projects;
}
