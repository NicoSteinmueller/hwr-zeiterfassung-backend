package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Integer> {
}
