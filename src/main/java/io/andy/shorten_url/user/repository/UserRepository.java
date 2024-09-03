package io.andy.shorten_url.user.repository;

import io.andy.shorten_url.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    int getUsageById(Long id);
}
