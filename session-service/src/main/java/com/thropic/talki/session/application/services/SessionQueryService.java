package com.thropic.talki.session.application.services;

import com.thropic.talki.session.domain.model.entities.Feedback;
import com.thropic.talki.session.domain.model.entities.Session;

import java.util.List;
import java.util.Optional;

/**
 * Lado Query del patrón CQRS aplicado al bounded context Practice Session.
 * Agrupa las operaciones de lectura sobre el agregado Session y sus
 * entidades asociadas, sin emitir efectos secundarios.
 */
public interface SessionQueryService {

    Optional<Session> findById(Long id);

    List<Session> findByUserId(Long userId);

    List<Feedback> findFeedbacksBySessionId(Long sessionId);
}
