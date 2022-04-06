package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.RoleRepository;
import com.hwr.hwrzeiterfassung.database.tables.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

//@DataJpaTest
@JdbcTest
@AutoConfigureMockMvc

public class RoleControllerTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void test() {
        Role role = new Role();
        role.setName("test");
        roleRepository.saveAndFlush(role);

        System.out.println(roleRepository.findAll().toArray());

    }
}
