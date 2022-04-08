package com.hwr.hwrzeiterfassung.controller;

import com.hwr.hwrzeiterfassung.database.controller.LoginController;
import com.hwr.hwrzeiterfassung.database.repositorys.HumanRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.LoginRepository;
import com.hwr.hwrzeiterfassung.database.repositorys.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path = "/update")
public class UpdateController {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private LoginController loginController;
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping(path = "/password")
    public @ResponseStatus
    HttpStatus changePassword(@RequestParam String email, @RequestParam String password, @RequestParam String newPassword) {
        loginController.validateLoginInformation(email, password);
        if (newPassword.length() != 256)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Hash from new Password is not valid.");
        var login = loginRepository.getById(email);
        login.setPassword(newPassword);
        loginRepository.saveAndFlush(login);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping(path = "/lastName")
    public @ResponseStatus
    HttpStatus changelastName(@RequestParam String email, @RequestParam String password, @RequestParam String newLastName) {
        loginController.validateLoginInformation(email, password);
        var human = humanRepository.getById(email);
        human.setLastName(newLastName);
        humanRepository.saveAndFlush(human);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping(path = "/targetDailyWorkingTime")
    public @ResponseStatus
    HttpStatus changetargetDailyWorkingTime(@RequestParam String email, @RequestParam String password, @RequestParam double targetDailyWorkingTime) {
        loginController.validateLoginInformation(email, password);
        var human = humanRepository.getById(email);
        human.setTargetDailyWorkingTime(targetDailyWorkingTime);
        humanRepository.saveAndFlush(human);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping(path = "/defaultProjectId")
    public @ResponseStatus
    HttpStatus changedefaultProjectId(@RequestParam String email, @RequestParam String password, @RequestParam int projectId) {
        loginController.validateLoginInformation(email, password);
        var human = humanRepository.getById(email);
        human.setDefaultProject(projectRepository.getById(projectId));
        humanRepository.saveAndFlush(human);
        return HttpStatus.ACCEPTED;
    }

}
