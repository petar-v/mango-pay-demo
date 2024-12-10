package org.flutterbit.mangopay.demo.service;

import jakarta.inject.Singleton;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import org.flutterbit.mangopay.demo.security.PasswordEncoder;

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

        return userRepository.save(user);
    }
}
