package org.flutterbit.mangopay.demo.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Introspected
@Data
@Serdeable
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ProjectIdea projectIdea;

    @ManyToOne
    private CoolUser author;

    private String text;
}