package org.flutterbit.mangopay.demo.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.model.ProjectIdea;
import org.flutterbit.mangopay.demo.service.CoolUserService;
import org.flutterbit.mangopay.demo.service.ProjectIdeaService;

import java.util.List;
import java.util.stream.Collectors;

@Introspected
@Serdeable
record AuthorResponse(Long id, String name, String email) {
}

@Introspected
@Serdeable
record CommentResponse(Long id, String text, AuthorResponse author) {
}

@Introspected
@Serdeable
record DetailedIdeaResponse(Long id, String name, String description, String imageUrl, AuthorResponse author,
                            int likeCount, List<CommentResponse> comments, boolean isLikedByCurrentUser) {
}

@Introspected
@Serdeable
record IdeaResponse(Long id, String name, String description, int commentCount, int likeCount, AuthorResponse author) {
}

@Introspected
@Serdeable
record NewCommentRequest(String text) {
}

@Introspected
@Serdeable
record NewIdeaRequest(String name, String description) {
}

@Slf4j
@Controller("/ideas")
public class ProjectIdeaController {

    @Inject
    private final ProjectIdeaService projectIdeaService;

    @Inject
    private final CoolUserService userService;

    public ProjectIdeaController(ProjectIdeaService projectIdeaService, CoolUserService userService) {
        this.projectIdeaService = projectIdeaService;
        this.userService = userService;
    }

    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    public List<IdeaResponse> listIdeas() {
        return projectIdeaService.listAllIdeas().stream().map(idea -> new IdeaResponse(
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
        )).collect(Collectors.toList());
    }

    @Get("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public DetailedIdeaResponse getIdeaById(@PathVariable Long id, @Header(value = "Authorization", defaultValue = "") String authorizationHeader) {
        ProjectIdea idea = projectIdeaService.getIdeaById(id);
        boolean isLikedByCurrentUser = false;
        if (!authorizationHeader.isBlank()) {
            CoolUser user = userService.getUserFromToken(authorizationHeader);
            isLikedByCurrentUser = idea.getLikes().stream().anyMatch(likedUser -> likedUser.getId().equals(user.getId()));
        }

        return new DetailedIdeaResponse(
                idea.getId(),
                idea.getName(),
                idea.getDescription(),
                idea.getImage() != null ? idea.getImage().getUrl() : null,
                new AuthorResponse(
                        idea.getAuthor().getId(),
                        idea.getAuthor().getName(),
                        idea.getAuthor().getEmail()
                ),
                idea.getLikes().size(),
                idea.getComments().stream().map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        new AuthorResponse(
                                comment.getAuthor().getId(),
                                comment.getAuthor().getName(),
                                comment.getAuthor().getEmail()
                        )
                )).collect(Collectors.toList()),
                isLikedByCurrentUser
        );
    }

    @Put("/{id}/comments")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<String> addCommentToIdea(@PathVariable Long id, @Body NewCommentRequest request, @Header("Authorization") String authorizationHeader) {
        CoolUser user = userService.getUserFromToken(authorizationHeader);
        projectIdeaService.addCommentToIdea(id, request.text(), user);
        return HttpResponse.ok("Comment added");
    }

    @Post("/{id}/like")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<String> likeIdea(@PathVariable Long id, @Header("Authorization") String authorizationHeader) {
        CoolUser user = userService.getUserFromToken(authorizationHeader);
        projectIdeaService.likeIdea(id, user);
        return HttpResponse.ok("Article is liked");
    }

    @Post("/{id}/dislike")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<String> dislikeIdea(@PathVariable Long id, @Header("Authorization") String authorizationHeader) {
        CoolUser user = userService.getUserFromToken(authorizationHeader);
        projectIdeaService.dislikeIdea(id, user);
        return HttpResponse.ok("Article is disliked");
    }

    @Delete("/{id}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<String> deleteIdea(@PathVariable Long id, @Header("Authorization") String authorizationHeader) {
        CoolUser user = userService.getUserFromToken(authorizationHeader);
        projectIdeaService.deleteIdea(id, user);
        return HttpResponse.ok("Idea deleted successfully");
    }

    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Long> createIdea(@Body NewIdeaRequest request, @Header("Authorization") String authorizationHeader) {
        CoolUser user = userService.getUserFromToken(authorizationHeader);
        ProjectIdea idea = projectIdeaService.createIdea(request.name(), request.description(), user);
        return HttpResponse.ok(idea.getId());
    }
}