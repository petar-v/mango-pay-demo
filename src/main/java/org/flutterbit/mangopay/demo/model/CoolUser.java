package org.flutterbit.mangopay.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Introspected
@Data
@Serdeable
@NoArgsConstructor
@AllArgsConstructor
public class CoolUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Image avatar;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    public CoolUser(String name, String email, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}