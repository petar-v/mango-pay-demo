package org.flutterbit.mangopay.demo.model;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Introspected
@Data
class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private ProjectIdea projectIdea;

    @ManyToOne
    private User author;

    private String text;
}