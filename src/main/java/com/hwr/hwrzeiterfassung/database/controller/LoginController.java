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

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginRepository loginRepository;

    @PostMapping("/basicLogin")
    public ResponseEntity<HttpStatus> basicLogin(@RequestParam String email, @RequestParam String password) {
        if (validateLoginInformation(email, password)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public boolean validateLoginInformation(String email, String password) {
        Optional<Login> login = loginRepository.findById(email);
        if (login.isEmpty()) {
            return false;
        } else {
            return login.get().getPassword().equals(password);
        }

    }
}
