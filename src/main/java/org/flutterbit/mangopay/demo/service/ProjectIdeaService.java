package org.flutterbit.mangopay.demo.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.flutterbit.mangopay.demo.model.Comment;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.model.ProjectIdea;
import org.flutterbit.mangopay.demo.repository.CommentsRepository;
import org.flutterbit.mangopay.demo.repository.ProjectIdeaRepository;

import java.util.List;

@Slf4j
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
        projectIdeaRepository.save(idea);
    }

    @Transactional
    public void likeIdea(Long ideaId, CoolUser user) {
        ProjectIdea idea = getIdeaById(ideaId);
        idea.getLikes().add(user);
        projectIdeaRepository.save(idea);
    }

    @Transactional
    public void dislikeIdea(Long ideaId, CoolUser user) {
        ProjectIdea idea = getIdeaById(ideaId);
        idea.getLikes().remove(user);
        projectIdeaRepository.save(idea);
    }

    @Transactional
    public void deleteIdea(Long ideaId, CoolUser user) {
        ProjectIdea idea = getIdeaById(ideaId);
        log.info("Deleting Idea: user ID = {}, author ID = {}", user.getId(), idea.getAuthor().getId());
        if (!idea.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this idea");
        }
        projectIdeaRepository.delete(idea);
    }

    @Transactional
    public ProjectIdea createIdea(String name, String description, CoolUser author) {
        ProjectIdea idea = new ProjectIdea();
        idea.setName(name);
        idea.setDescription(description);
        idea.setAuthor(author);
        return projectIdeaRepository.save(idea);
    }
}