package org.flutterbit.mangopay.demo.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestReactiveAuthenticationProvider;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import org.reactivestreams.Publisher;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH;
import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_NOT_FOUND;

@Singleton
class CoolUserAuthenticationProvider<B> implements HttpRequestReactiveAuthenticationProvider<B> {

    private final CoolUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Scheduler scheduler;

    CoolUserAuthenticationProvider(CoolUserRepository userRepository,
                                          PasswordEncoder passwordEncoder,
                                          @Named(TaskExecutors.BLOCKING) ExecutorService executorService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.scheduler = Schedulers.fromExecutorService(executorService);
    }

    @Override
    @NonNull
    public  Publisher<AuthenticationResponse> authenticate(
            @Nullable HttpRequest<B> requestContext,
            @NonNull AuthenticationRequest<String, String> authenticationRequest
    ) {
        return Flux.<AuthenticationResponse>create(emitter -> {
            CoolUser user = fetchUser(authenticationRequest);
            AuthenticationFailed authenticationFailed = validate(user, authenticationRequest);
            if (authenticationFailed != null) {
                emitter.error(new AuthenticationException(authenticationFailed));
            } else {
                emitter.next(createSuccessfulAuthenticationResponse(user));
                emitter.complete();
            }
        }, FluxSink.OverflowStrategy.ERROR).subscribeOn(scheduler);
    }

    private AuthenticationFailed validate(CoolUser user, AuthenticationRequest<?, ?> authenticationRequest) {
        AuthenticationFailed authenticationFailed = null;
        if (user == null) {
            authenticationFailed = new AuthenticationFailed(USER_NOT_FOUND);
        } else if (!passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            authenticationFailed = new AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH);
        }

        return authenticationFailed;
    }

    private CoolUser fetchUser(AuthenticationRequest<?, ?> authRequest) {
        final Object email = authRequest.getIdentity();
        return userRepository.findByEmail(email.toString()).orElse(null);
    }

    private AuthenticationResponse createSuccessfulAuthenticationResponse(CoolUser user) {
        return AuthenticationResponse.success(user.getEmail());
    }
}