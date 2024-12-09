package org.flutterbit.mangopay.demo.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Introspected
@Data
@Serdeable
public class CoolUser {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Image avatar;

    public CoolUser() {}

    public CoolUser(String name) {
        this.name = name;
    }
}