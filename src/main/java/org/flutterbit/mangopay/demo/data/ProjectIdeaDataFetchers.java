package org.flutterbit.mangopay.demo.data;

import graphql.schema.DataFetcher;
import jakarta.inject.Singleton;
import org.flutterbit.mangopay.demo.controller.AuthorResponse;
import org.flutterbit.mangopay.demo.controller.IdeaResponse;
import org.flutterbit.mangopay.demo.service.ProjectIdeaService;

import java.util.List;

@Singleton
public class ProjectIdeaDataFetchers {

    private final ProjectIdeaService projectIdeaService;

    public ProjectIdeaDataFetchers(ProjectIdeaService projectIdeaService) {
        this.projectIdeaService = projectIdeaService;
    }

    public DataFetcher<List<IdeaResponse>> listIdeasFetcher() {
        return dataFetchingEnvironment -> projectIdeaService.listAllIdeas().stream()
                .map(idea -> new IdeaResponse(
                        idea.getId(),
                        idea.getName(),
                        idea.getDescription(),
                        idea.getComments().size(),
                        idea.getLikes().size(),
                        new AuthorResponse(
                                idea.getAuthor().getId(),
                                idea.getAuthor().getName(),
                                idea.getAuthor().getEmail()
                        )
                ))
                .toList();
    }
}
