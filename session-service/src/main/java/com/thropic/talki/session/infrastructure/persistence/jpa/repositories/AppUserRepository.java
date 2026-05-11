package com.thropic.talki.session.infrastructure.persistence.jpa.repositories;

import com.thropic.talki.session.domain.model.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
