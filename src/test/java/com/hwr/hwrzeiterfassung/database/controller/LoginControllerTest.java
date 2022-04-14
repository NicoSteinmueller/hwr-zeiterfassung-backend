package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.LoginRepository;
import com.hwr.hwrzeiterfassung.database.tables.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Mock
    LoginRepository loginRepository;
    @InjectMocks
    LoginController loginController;

    @BeforeEach
    void basicSetup() {
        String email = "test";
        String rightPwd = "right";
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(rightPwd);
        when(loginRepository.findById(email)).thenReturn(Optional.of(login));
    }

    @Test
    void basicLogin() {
        String email = "test";
        String rightPwd = "right";

        var result0 = loginController.basicLogin(email, rightPwd);
        assertEquals(new ResponseEntity<>(HttpStatus.ACCEPTED), result0);
    }

    @Test
    void basicWrongPassword() {
        String email = "test";
        String wrongPwd = "wrong";

        ResponseStatusException e1 = assertThrows(ResponseStatusException.class, () -> loginController.validateLoginInformation(email, wrongPwd));
        assertEquals("User Credentials invalid", e1.getReason());
    }

    @Test
    void basicWrongUser() {
        String wrongUser = "Vanish";
        String rightPwd = "right";

        ResponseStatusException e1 = assertThrows(ResponseStatusException.class, () -> loginController.validateLoginInformation(wrongUser, rightPwd));
        assertEquals("User Credentials invalid", e1.getReason());
    }

    @Test
    void validateLoginInformation() {
        String email = "test";
        String rightPwd = "right";

        assertDoesNotThrow(() -> loginController.validateLoginInformation(email, rightPwd));
    }

    @Test
    void validateWrongPwd() {
        String email = "test";
        String wrongPwd = "wrong";

        ResponseStatusException e1 = assertThrows(ResponseStatusException.class, () -> loginController.validateLoginInformation(email, wrongPwd));
        assertEquals("User Credentials invalid", e1.getReason());
    }

    @Test
    void validateWrongUser() {
        String wrongUser = "Vanish";
        String rightPwd = "right";

        ResponseStatusException e2 = assertThrows(ResponseStatusException.class, () -> loginController.validateLoginInformation(wrongUser, rightPwd));
        assertEquals("User Credentials invalid", e2.getReason());
    }
}
