package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Login;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to access all logins
 */
public interface LoginRepository extends JpaRepository<Login, String> {
}
