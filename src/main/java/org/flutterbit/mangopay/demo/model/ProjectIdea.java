package org.flutterbit.mangopay.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Introspected
@Data
@Serdeable
public class ProjectIdea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private Image image;

    @ManyToOne
    private CoolUser author;

    @OneToMany(mappedBy = "projectIdea", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Comment> comments;

    @ManyToMany
    @ToString.Exclude
    @JsonIgnore
    private Set<CoolUser> likes;

    public ProjectIdea() {
        this.comments = List.of();
        this.likes = Set.of();
    }

    public ProjectIdea(String name, String description, CoolUser author) {
        this();
        this.name = name;
        this.description = description;
        this.author = author;
    }
}
