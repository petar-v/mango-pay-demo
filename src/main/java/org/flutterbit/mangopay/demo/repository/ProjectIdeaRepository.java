package org.flutterbit.mangopay.demo.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.flutterbit.mangopay.demo.model.ProjectIdea;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectIdeaRepository extends JpaRepository<ProjectIdea, Long> {

    @EntityGraph(attributePaths = {"comments", "likes", "author", "comments.author", "image"})
    List<ProjectIdea> findAll();

    @EntityGraph(attributePaths = {"comments", "likes", "author", "comments.author", "image"})
    Optional<ProjectIdea> findById(Long id);
}
