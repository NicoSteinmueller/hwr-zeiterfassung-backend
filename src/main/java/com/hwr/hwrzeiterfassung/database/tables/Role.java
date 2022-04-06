package com.hwr.hwrzeiterfassung.database.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "role")
    @JsonIgnore
    private Human human;
}
