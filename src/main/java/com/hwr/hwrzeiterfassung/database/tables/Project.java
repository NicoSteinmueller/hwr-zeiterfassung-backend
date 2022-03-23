package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(256)")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @OneToMany(mappedBy = "project")
    private Set<Time> time;

    @ManyToMany(mappedBy = "projects")
    private Set<Human> humans;
}
