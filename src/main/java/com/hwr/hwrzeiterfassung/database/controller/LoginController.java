package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.LoginRepository;
import com.hwr.hwrzeiterfassung.database.tables.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Controller for the Login and the Login validation
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginRepository loginRepository;

    /**
     * Login for the app / website
     *
     * @param email    the email of the human
     * @param password hashed password of the human
     * @return HttpStatus Accepted or throw Not_Acceptable
     */
    @PostMapping("/basicLogin")
    public ResponseEntity<HttpStatus> basicLogin(@RequestParam String email, @RequestParam String password) {
        validateLoginInformation(email, password);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * method to prof the Login information, that throw an exception, when the Credentials are invalid
     *
     * @param email    the email of the human
     * @param password the hashed password of the human
     */
    public void validateLoginInformation(String email, String password) {
        Optional<Login> login = loginRepository.findById(email);
        if (login.isEmpty() || !login.get().getPassword().equals(password))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");
    }
}
