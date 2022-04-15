package com.hwr.hwrzeiterfassung.database.controller;

import com.hwr.hwrzeiterfassung.database.repositorys.ProjectRepository;
import com.hwr.hwrzeiterfassung.database.tables.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * get a Project by ID, throw Exception if no Project with this ID exits
     *
     * @param projectId the ID of the Project
     * @return Project
     */
    public Project getProjectById(int projectId) {
        var project = projectRepository.findById(projectId);
        if (project.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No Project with this id exists");
        return project.get();
    }
}
