package com.thropic.talki.session.infrastructure.persistence.jpa.repositories;

import com.thropic.talki.session.domain.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
