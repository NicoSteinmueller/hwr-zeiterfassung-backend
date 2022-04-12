package com.hwr.hwrzeiterfassung.database.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
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
    @JsonIgnore
    private Set<Time> time;

    @OneToMany(mappedBy = "defaultProject")
    @JsonIgnore
    private Set<Human> defaultHumans;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnore
    private Set<Human> humans;
}
