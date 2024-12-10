package org.flutterbit.mangopay.demo.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.security.token.render.AccessRefreshToken;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import org.flutterbit.mangopay.demo.security.PasswordEncoder;
import org.flutterbit.mangopay.demo.service.CoolUserService;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Introspected
@Serdeable.Deserializable
record RegisterRequest(
        @NotBlank String email,
        @NotBlank String name,
        @NotBlank String password
) {
}

@Introspected
@Serdeable.Deserializable
@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest implements AuthenticationRequest {
    @NotBlank
    private String identity;

    @NotBlank
    private String secret;
}

@Slf4j
@Controller("/users")
public class CoolUserController {
    @Inject
    private CoolUserRepository userRepository;

    @Inject
    private CoolUserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private JwtTokenGenerator jwtTokenGenerator;

    @Put("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<String> register(@Body @Valid RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return HttpResponse.badRequest("Email already exists");
        }
        userService.registerUser(request.name(), request.email(), request.password());

        return HttpResponse.ok("User registered successfully");
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/test-secured")
    public HttpResponse<String> securedEndpoint() {
        return HttpResponse.ok("You are successfully authenticated and can access this endpoint!");
    }

    private Optional<String> generateJwtToken(CoolUser user) {
        Map<String, Object> claims = Map.of("sub", user.getId(), "email", user.getEmail(), "name", user.getName());
        return jwtTokenGenerator.generateToken(claims);
    }

    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Mono<HttpResponse<AccessRefreshToken>> login(@Body @Valid LoginRequest request) {
        return Mono.create(emitter -> {
            userRepository.findByEmail(request.getIdentity()).ifPresentOrElse(user -> {
                log.info("Request secret: {}, User password: {}", request.getSecret(), user.getPassword());
                if (passwordEncoder.matches(request.getSecret(), user.getPassword())) {
                    Optional<String> tokenOptional = generateJwtToken(user);

                    if (tokenOptional.isPresent()) {
                        String token = tokenOptional.get();
                        AccessRefreshToken accessToken = new AccessRefreshToken(token, null, user.getEmail(), null);
                        log.info("User logged in successfully: {}", user.getEmail());
                        emitter.success(HttpResponse.ok(accessToken));
                    } else {
                        emitter.error(new AuthenticationException("Token generation failed"));
                    }
                } else {
                    emitter.error(new AuthenticationException("Invalid credentials"));
                }
            }, () -> emitter.error(new AuthenticationException("User not found")));
        });
    }
}
