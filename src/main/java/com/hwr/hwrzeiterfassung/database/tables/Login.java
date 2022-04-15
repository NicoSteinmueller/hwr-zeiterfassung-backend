package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * this Entity is for the Login information of a Human
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Login {
    /**
     * the email for the login
     */
    @Id
    @Column
    private String email;

    /**
     * the password for the Login saved as a Hash
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    /**
     * link the email from the login to a human
     */
    @OneToOne
    @MapsId
    @JoinColumn(columnDefinition = "VARCHAR(320)")
    private Human human;
}
