package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.RoleRepository;
import com.hwr.hwrzeiterfassung.database.tables.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

public class RoleControllerTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void test() {
        assertTrue(true);
        //TODO write real test
    }
}
