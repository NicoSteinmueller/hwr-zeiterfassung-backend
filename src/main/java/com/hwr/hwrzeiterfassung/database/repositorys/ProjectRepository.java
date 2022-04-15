package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to access all projects
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
