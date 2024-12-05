package org.flutterbit.mangopay.demo.model;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Entity
@Introspected
@Data
public class ProjectIdea {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private Image image;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "projectIdea", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany
    private Set<User> likes;
}