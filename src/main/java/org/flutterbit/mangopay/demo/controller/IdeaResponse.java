package org.flutterbit.mangopay.demo.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record IdeaResponse(Long id, String name, String description, int commentCount, int likeCount,
                           AuthorResponse author) {
}
