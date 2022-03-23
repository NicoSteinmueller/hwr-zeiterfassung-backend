package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Human;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumanRepository extends JpaRepository<Human, String> {
}
