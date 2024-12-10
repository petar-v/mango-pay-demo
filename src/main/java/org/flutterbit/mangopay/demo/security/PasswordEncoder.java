package org.flutterbit.mangopay.demo.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface PasswordEncoder {
    String encode(@NotBlank @NotNull String rawPassword);

    boolean matches(@NotBlank @NotNull String rawPassword,
                    @NotBlank @NotNull String encodedPassword);
}
