package org.flutterbit.mangopay.demo.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.flutterbit.mangopay.demo.model.ProjectIdea;
import org.flutterbit.mangopay.demo.service.ProjectIdeaService;

import java.util.List;

@Controller("/ideas")
public class ProjectIdeaController {

    private final ProjectIdeaService projectIdeaService;

    public ProjectIdeaController(ProjectIdeaService projectIdeaService) {
        this.projectIdeaService = projectIdeaService;
    }

    @Get
    public List<ProjectIdea> listIdeas() {
        return projectIdeaService.listAllIdeas();
    }
}
