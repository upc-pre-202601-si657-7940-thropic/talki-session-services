package com.thropic.talki.livecoach.domain.model;

public class QuickPracticeStrategy implements SessionModeStrategy {
    @Override public String getMode() { return "quick_practice"; }
    @Override public int maxDurationMinutes() { return 3; }
    @Override
    public String buildSystemPrompt(String scenarioId) {
        return "Eres un coach de comunicación silencioso. Motiva al inicio y da UN único feedback al final.";
    }
}
