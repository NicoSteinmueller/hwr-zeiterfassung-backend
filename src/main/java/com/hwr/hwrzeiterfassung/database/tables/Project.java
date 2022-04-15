package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * this entity is for all data of a project
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Project {
    /**
     * the id for clearly identify on time in the DB
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    /**
     * the name of the project
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(256)")
    private String name;

    /**
     * a detailed description of the project
     */
    @Column(columnDefinition = "TEXT")
    private String description;
}
