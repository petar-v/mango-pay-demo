package org.flutterbit.mangopay.demo.mangopay.demo.model;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Introspected
@Data
class CoolUser {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Image avatar;
}