package org.flutterbit.mangopay.demo.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.flutterbit.mangopay.demo.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
