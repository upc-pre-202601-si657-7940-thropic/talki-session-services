package com.thropic.talki.livecoach.domain.model;

public interface SessionModeStrategy {
    String getMode();
    String buildSystemPrompt(String scenarioId);
    int maxDurationMinutes();
}
