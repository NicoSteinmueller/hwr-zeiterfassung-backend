package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.tables.Human;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for the human
 */
@Controller
@RequestMapping(path = "/human")
public class HumanController {
    @Autowired
    private LoginController loginController;
    @Autowired
    private HumanRepository humanRepository;

    /**
     * get all project, for them the human has access
     *
     * @param email    email for Login validation
     * @param password hashed password for Login validation
     * @return Iterable of projects
     */
    @GetMapping(path = "/getAllProjects")
    public @ResponseBody
    Iterable<Project> getProjectsWithUserAccess(@RequestParam String email, @RequestParam String password) {
        loginController.validateLoginInformation(email, password);

        var human = getHumanByEmail(email);
        return human.getProjects();
    }

    /**
     * request the name of the human
     *
     * @param email    email for Login validation
     * @param password hashed password for Login validation
     * @return first and last name as map
     */
    @GetMapping(path = "/name")
    public @ResponseBody
    Map<String, String> getHumanName(@RequestParam String email, @RequestParam String password) {
        loginController.validateLoginInformation(email, password);

        var human = getHumanByEmail(email);

        Map<String, String> map = new HashMap<>();
        map.put("firstName", human.getFirstName());
        map.put("lastName", human.getLastName());
        return map;
    }

    /**
     * request the default project from the human
     *
     * @param email    email for Login validation
     * @param password hashed password for Login validation
     * @return default project
     */
    @GetMapping(path = "/getDefaultProject")
    public @ResponseBody
    Optional<Project> getDefaultProject(@RequestParam String email, @RequestParam String password) {
        loginController.validateLoginInformation(email, password);

        return Optional.ofNullable(getHumanByEmail(email).getDefaultProject());
    }

    /**
     * get the Human with this mail or throw Exception
     *
     * @param email email from the human
     * @return Human
     */
    public Human getHumanByEmail(String email) {
        var human = humanRepository.findById(email);
        if (human.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Human doesn't exists");
        return human.get();
    }

}
