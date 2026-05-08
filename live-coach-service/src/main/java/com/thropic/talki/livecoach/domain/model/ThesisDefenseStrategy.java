package com.thropic.talki.livecoach.domain.model;

public class ThesisDefenseStrategy implements SessionModeStrategy {
    @Override public String getMode() { return "thesis_defense"; }
    @Override public int maxDurationMinutes() { return 20; }
    @Override
    public String buildSystemPrompt(String scenarioId) {
        return "Eres un jurado académico exigente. Formula 3 preguntas metodológicas sobre la tesis presentada.";
    }
}
