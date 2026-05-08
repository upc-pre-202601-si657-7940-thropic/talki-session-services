package com.thropic.talki.livecoach.domain.model;

import org.springframework.stereotype.Component;

@Component
public class SessionModeStrategyFactory {
    public SessionModeStrategy create(String mode) {
        return switch (mode) {
            case "quick_practice" -> new QuickPracticeStrategy();
            case "interview"      -> new InterviewStrategy();
            case "thesis_defense" -> new ThesisDefenseStrategy();
            case "scenario"       -> new ScenarioStrategy();
            default -> throw new IllegalArgumentException("Unknown session mode: " + mode);
        };
    }
}
