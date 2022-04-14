package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class HumanControllerTest {
    @Mock
    LoginController loginController;
    // wird benötigt
    @Mock
    HumanRepository humanRepository;
    @InjectMocks
    HumanController humanController;

    @Test
    void getProjectsWithUserAccess(){
        String email = "Test";
        String password = "Test";
        Human human = new Human();
        human.setEmail("Test");
        human.setFirstName("test");
        human.setLastName("test");
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.of(human));
        Project project = new Project();
        project.setName("test");
        Set<Project> projects = new HashSet<>();
        projects.add(project);
        human.setProjects(projects);
        var result = humanController.getProjectsWithUserAccess(email, password);
        assertThat(result).hasSize(1);
        assertEquals(projects, result);
    }

    @Test
    void humanHasNoProject(){
        String email = "Test";
        String password = "Test";
        Human human = new Human();
        human.setEmail("Test");
        human.setFirstName("test");
        human.setLastName("test");
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.of(human));
        human.setProjects(new HashSet<>());
        var result = humanController.getProjectsWithUserAccess(email, password);
        assertThat(result).isEmpty();
    }

    @Test
    void getHumanName(){
        String email = "Test";
        String password = "Test";
        Human human = new Human();
        human.setEmail("Test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.of(human));

        var result = humanController.getHumanName(email, password);
        assertEquals("Tester", result.get("firstName"));
        assertEquals("Van Test", result.get("lastName"));
    }

    @Test
    void getDefaultProject(){
        String email = "Test";
        String password = "Test";

        Human human = new Human();
        human.setEmail("Test");
        human.setFirstName("Tester");
        human.setLastName("Van Test");
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.of(human));
        Project project = new Project();
        project.setName("test");
        human.setDefaultProject(project);

        var result = humanController.getDefaultProject(email, password);
        assertEquals("test", result.getName());
    }

    @Test
    void noDefaultProject(){
        String email = "Test";
        String password = "Test";
        Human human = new Human();
        human.setEmail("Test");
        human.setFirstName("test");
        human.setLastName("test");
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.of(human));
        Project project = new Project();
        project.setName("test");
        Set<Project> projects = new HashSet<>();
        projects.add(project);
        human.setProjects(projects);

        var result = humanController.getDefaultProject(email, password);
        assertEquals(null, result);
        //Vielleicht wollen wir nicht null zurück?
    }

    @Test
    void getHumanByEmail() {
        String email = "Test";
        Human human = new Human();
        human.setEmail("Test");
        human.setFirstName("test");
        human.setLastName("test");
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.of(human));

        var result = humanController.getHumanByEmail(email);
    }

    @Test
    void noHuman() {
        when(humanRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        var e = assertThrows(ResponseStatusException.class, () -> humanController.getHumanByEmail("Test"));
        assertEquals("Human doesn't exists", e.getReason());
    }
}
