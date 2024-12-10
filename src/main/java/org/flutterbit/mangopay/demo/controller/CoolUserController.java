package org.flutterbit.mangopay.demo.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import org.flutterbit.mangopay.demo.security.PasswordEncoder;
import reactor.core.publisher.Mono;

@Introspected
@Serdeable.Deserializable
record RegisterRequest(
        @NotBlank String email,
        @NotBlank String name,
        @NotBlank String password
) {
}

@Controller("/users")
public class CoolUserController {

    @Inject
    private CoolUserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> register(@Body @Valid RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return HttpResponse.badRequest("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        CoolUser user = new CoolUser(request.name(), request.email(), encodedPassword);

        userRepository.save(user);
        return HttpResponse.ok("User registered successfully");
    }


    @Post("/login")
    public Mono<HttpResponse<AuthenticationResponse>> login(@Body AuthenticationRequest request) {
        return Mono.create(emitter -> {
            userRepository.findByName(request.getIdentity().toString()).ifPresentOrElse(user -> {
                if (passwordEncoder.matches(request.getSecret().toString(), user.getPassword())) {
                    emitter.success(HttpResponse.ok(AuthenticationResponse.success(user.getName())));
                } else {
                    emitter.error(new AuthenticationException("Invalid credentials"));
                }
            }, () -> emitter.error(new AuthenticationException("User not found")));
        });
    }
}
