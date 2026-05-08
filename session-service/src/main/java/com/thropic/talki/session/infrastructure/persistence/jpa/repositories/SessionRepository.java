package com.thropic.talki.session.infrastructure.persistence.jpa.repositories;

import com.thropic.talki.session.domain.model.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUserId(Long userId);
}
