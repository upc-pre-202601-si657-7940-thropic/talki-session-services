package com.thropic.talki.session.application.services;

import com.thropic.talki.session.domain.model.entities.Feedback;
import com.thropic.talki.session.domain.model.entities.Session;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.FeedbackRepository;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SessionQueryServiceImpl implements SessionQueryService {

    private final SessionRepository sessionRepository;
    private final FeedbackRepository feedbackRepository;

    public SessionQueryServiceImpl(SessionRepository sessionRepository,
                                    FeedbackRepository feedbackRepository) {
        this.sessionRepository = sessionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Optional<Session> findById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public List<Session> findByUserId(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    @Override
    public List<Feedback> findFeedbacksBySessionId(Long sessionId) {
        return feedbackRepository.findBySessionId(sessionId);
    }
}
