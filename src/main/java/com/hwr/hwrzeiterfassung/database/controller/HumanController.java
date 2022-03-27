package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path = "/human")
public class HumanController {
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private LoginController loginController;

    @GetMapping(path = "/getAllProjects")
    public @ResponseBody
    Iterable<Project> getProjectsWithUserAccess(@RequestParam String email, @RequestParam String password) {

        if (!loginController.validateLoginInformation(email, password)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User Credentials invalid");
        }
        var human = humanRepository.findById(email);
        if (human.isPresent()) {
            return human.get().getProjects();
        }
        return (Iterable<Project>) new Project();

    }

}
