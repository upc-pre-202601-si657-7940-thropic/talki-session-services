package com.thropic.talki.session.infrastructure.persistence.jpa.repositories;

import com.thropic.talki.session.domain.model.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySessionId(Long sessionId);
}
