package com.hwr.hwrzeiterfassung.controller;

import com.google.common.hash.Hashing;
import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.LoginRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.ProjectRepository;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import com.hwr.hwrzeiterfassung.database.tables.Login;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UpdateControllerTest {
    @Mock
    LoginController loginController;
    @Mock
    LoginRepository loginRepository;
    @Mock
    HumanRepository humanRepository;
    @Mock
    ProjectRepository projectRepository;
    @InjectMocks
    UpdateController updateController;

    @BeforeEach
    void setUp() {



    }

    @Test
    void passwordValid() {
        Login login = new Login();
        login.setEmail("test");
        login.setPassword("1234");
        when(loginRepository.getById(Mockito.anyString())).thenReturn(login);
        String newPwd = Hashing.sha256().hashString("New", StandardCharsets.UTF_8).toString();
        var result = updateController.password("", "", newPwd);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
        assertEquals(newPwd, login.getPassword());
    }

    @Test
    void passwordInvalid() {
        var e = assertThrows(ResponseStatusException.class, () ->
                updateController.password("", "", ""));
        assertEquals("Hash from new Password is not valid.", e.getReason());
    }

    @Test
    void lastName() {
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        when(humanRepository.getById(Mockito.anyString())).thenReturn(human);
        var result = updateController.lastName("", "", "Newton");
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
        assertEquals("Newton", human.getLastName());
    }

    @Test
    void targetDailyWorkingTime() {
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        human.setTargetDailyWorkingTime(6.0);
        when(humanRepository.getById(Mockito.anyString())).thenReturn(human);
        var result = updateController.targetDailyWorkingTime("", "", 8.0);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
        assertEquals(8.0, human.getTargetDailyWorkingTime());
    }

    @Test
    void defaultProjectId() {
        Human human = new Human();
        human.setEmail("test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        Project project = new Project();
        project.setName("Test");
        human.setDefaultProject(project);
        Project newProject = new Project();
        newProject.setName("New Project");
        when(projectRepository.getById(Mockito.any())).thenReturn(newProject);
        when(humanRepository.getById(Mockito.anyString())).thenReturn(human);
        var result = updateController.defaultProjectId("", "", newProject.getId());
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result);
        assertEquals(newProject, human.getDefaultProject());
    }
}