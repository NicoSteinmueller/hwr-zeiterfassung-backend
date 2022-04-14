package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.RoleRepository;
import com.hwr.hwrzeiterfassung.database.tables.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {
    @Mock
    RoleRepository roleRepository;
    @InjectMocks
    RoleController roleController;

    @Test
    void getAllRoles() {
        Role role0 = new Role();
        role0.setName("Role0");
        Role role1 = new Role();
        role1.setName("Role1");

        List<Role> allRoles = new ArrayList<>();
        allRoles.add(role0);
        allRoles.add(role1);
        when(roleRepository.findAll()).thenReturn(allRoles);

        List<Role> result = (List<Role>) roleController.getAllRoles();
        assertThat(result).hasSize(2);
        assertEquals("Role1", result.get(1).getName());
    }

    @Test
    void emptyList() {
        List<Role> allRoles = new ArrayList<>();
        when(roleRepository.findAll()).thenReturn(allRoles);

        List<Role> result = (List<Role>) roleController.getAllRoles();
        assertThat(result).isEmpty();
    }
}
