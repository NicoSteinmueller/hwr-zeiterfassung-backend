package com.hwr.hwrzeiterfassung.database.tables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * this Entity is to save the role of the human, which is later used fpr authorization management
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Role {
    /**
     * the name and id of role (every role is unique)
     */
    @Id
    @Column
    private String name;
}
