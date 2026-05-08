package com.thropic.talki.livecoach.domain.model;

public class ScenarioStrategy implements SessionModeStrategy {
    @Override public String getMode() { return "scenario"; }
    @Override public int maxDurationMinutes() { return 15; }
    @Override
    public String buildSystemPrompt(String scenarioId) {
        if (scenarioId == null || scenarioId.isBlank())
            throw new IllegalArgumentException("scenarioId requerido para modo scenario");
        return "Eres el personaje del escenario peruano '" + scenarioId + "'. Sigue el rol definido en el catálogo.";
    }
}
