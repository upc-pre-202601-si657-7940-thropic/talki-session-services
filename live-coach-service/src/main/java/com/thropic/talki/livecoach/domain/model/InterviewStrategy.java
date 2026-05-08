package com.thropic.talki.livecoach.domain.model;

public class InterviewStrategy implements SessionModeStrategy {
    @Override public String getMode() { return "interview"; }
    @Override public int maxDurationMinutes() { return 15; }
    @Override
    public String buildSystemPrompt(String scenarioId) {
        return "Eres un reclutador de empresa peruana. Realiza exactamente 5 preguntas progresivas de entrevista.";
    }
}
