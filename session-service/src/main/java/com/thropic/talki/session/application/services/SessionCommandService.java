package com.thropic.talki.session.application.services;

import com.thropic.talki.session.domain.model.entities.Feedback;
import com.thropic.talki.session.domain.model.entities.Session;

/**
 * Lado Command del patrón CQRS aplicado al bounded context Practice Session.
 * Agrupa las operaciones de escritura (creación, finalización, cambios de
 * estado) del agregado Session y de sus entidades asociadas.
 */
public interface SessionCommandService {

    Session create(Long userId, String title, String sessionType);

    Session finalize(Long sessionId);

    Feedback addFeedback(Long sessionId, String feedbackType, String content);
}
