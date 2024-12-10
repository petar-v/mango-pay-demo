package org.flutterbit.mangopay.demo.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record AuthorResponse(Long id, String name, String email) {
}
