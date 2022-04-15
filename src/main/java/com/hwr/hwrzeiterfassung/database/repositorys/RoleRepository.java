package com.hwr.hwrzeiterfassung.database.repositorys;

import com.hwr.hwrzeiterfassung.database.tables.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to access all roles
 */
public interface RoleRepository extends JpaRepository<Role, String> {

}
