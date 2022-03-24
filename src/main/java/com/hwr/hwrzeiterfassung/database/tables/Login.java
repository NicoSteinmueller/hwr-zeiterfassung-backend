package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
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
