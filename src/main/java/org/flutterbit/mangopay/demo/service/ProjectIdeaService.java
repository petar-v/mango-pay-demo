package org.flutterbit.mangopay.demo.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.flutterbit.mangopay.demo.model.ProjectIdea;
import org.flutterbit.mangopay.demo.repository.ProjectIdeaRepository;

import java.util.List;

@Singleton
public class ProjectIdeaService {

    private final ProjectIdeaRepository projectIdeaRepository;

    public ProjectIdeaService(ProjectIdeaRepository projectIdeaRepository) {
        this.projectIdeaRepository = projectIdeaRepository;
    }

    @Transactional
    public List<ProjectIdea> listAllIdeas() {
        return projectIdeaRepository.findAll();
    }
}
