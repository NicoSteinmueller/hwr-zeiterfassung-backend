package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Login {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;


    @OneToOne
    @MapsId
    @JoinColumn(name = "human_email", columnDefinition = "VARCHAR(320)")
    private Human human;
}
