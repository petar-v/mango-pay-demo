package org.flutterbit.mangopay.demo.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.security.authentication.AuthenticationException;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import org.flutterbit.mangopay.demo.security.PasswordEncoder;

import java.text.ParseException;

@Slf4j
@Singleton
public class CoolUserService {

    private final CoolUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CoolUserService(CoolUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CoolUser registerUser(String name, String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        CoolUser user = new CoolUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return user;
    }

    public CoolUser getUserFromToken(@NotNull String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        try {
            JWTClaimsSet claims = JWTParser.parse(token).getJWTClaimsSet();
            String userId = claims.getStringClaim("sub");
            log.info("**** USER ID FROM TOKEN: {} ****", userId);
            return userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new AuthenticationException("User not found"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new AuthenticationException("Invalid JWT token");
        }
    }
}
