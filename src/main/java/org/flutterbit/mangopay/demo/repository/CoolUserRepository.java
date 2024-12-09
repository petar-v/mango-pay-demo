package org.flutterbit.mangopay.demo.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import org.flutterbit.mangopay.demo.model.CoolUser;
import java.util.Optional;

@Repository
public interface CoolUserRepository extends JpaRepository<CoolUser, Long> {
    Optional<CoolUser> findByName(String name);
    Optional<CoolUser> findByEmail(String email);
    Optional<CoolUser> findById(Long id);
}