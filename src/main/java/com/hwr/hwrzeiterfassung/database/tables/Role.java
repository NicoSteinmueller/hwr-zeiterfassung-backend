package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "role")
    private Human human;
}
