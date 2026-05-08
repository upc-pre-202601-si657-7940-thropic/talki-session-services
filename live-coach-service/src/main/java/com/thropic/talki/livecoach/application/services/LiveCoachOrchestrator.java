package com.thropic.talki.livecoach.application.services;

import com.thropic.talki.livecoach.domain.event.SessionLiveFinalizedEvent;
import com.thropic.talki.livecoach.domain.model.SessionModeStrategy;
import com.thropic.talki.livecoach.domain.model.SessionModeStrategyFactory;
import com.thropic.talki.livecoach.infrastructure.messaging.producer.SessionFinalizedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LiveCoachOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(LiveCoachOrchestrator.class);

    private final SessionModeStrategyFactory strategyFactory;
    private final SessionFinalizedPublisher publisher;

    public LiveCoachOrchestrator(SessionModeStrategyFactory strategyFactory,
                                  SessionFinalizedPublisher publisher) {
        this.strategyFactory = strategyFactory;
        this.publisher = publisher;
    }

    public String prepareSystemPrompt(String mode, String scenarioId) {
        SessionModeStrategy strategy = strategyFactory.create(mode);
        String prompt = strategy.buildSystemPrompt(scenarioId);
        log.info("[live-coach-service] System prompt prepared for mode={}", mode);
        return prompt;
    }

    public SessionLiveFinalizedEvent finalizeAndPublish(String sessionId, String userId,
                                                         String mode, String scenarioId,
                                                         String audioUri, int durationSeconds,
                                                         String academicSegment) {
        SessionLiveFinalizedEvent event = new SessionLiveFinalizedEvent(
                sessionId, userId, mode, scenarioId, audioUri, durationSeconds, academicSegment
        );
        publisher.publish(event);
        return event;
    }
}
