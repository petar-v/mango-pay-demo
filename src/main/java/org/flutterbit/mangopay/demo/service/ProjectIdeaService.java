package org.flutterbit.mangopay.demo.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.flutterbit.mangopay.demo.model.Comment;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.model.ProjectIdea;
import org.flutterbit.mangopay.demo.repository.CommentsRepository;
import org.flutterbit.mangopay.demo.repository.ProjectIdeaRepository;

import java.util.List;

@Singleton
public class ProjectIdeaService {


    @Inject
    private final ProjectIdeaRepository projectIdeaRepository;

    @Inject
    private final CommentsRepository commentRepository;


    public ProjectIdeaService(ProjectIdeaRepository projectIdeaRepository, CommentsRepository commentRepository) {
        this.projectIdeaRepository = projectIdeaRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public List<ProjectIdea> listAllIdeas() {
        return projectIdeaRepository.findAll();
    }

    @Transactional
    public ProjectIdea getIdeaById(Long id) {
        return projectIdeaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project Idea not found for ID: " + id));
    }

    public void addCommentToIdea(Long ideaId, String text, CoolUser author) {
        ProjectIdea idea = getIdeaById(ideaId);

        Comment comment = new Comment();
        comment.setProjectIdea(idea);
        comment.setText(text);
        comment.setAuthor(author);
        commentRepository.save(comment);

        idea.getComments().add(comment);
    }
}